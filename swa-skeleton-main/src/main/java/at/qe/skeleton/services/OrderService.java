package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.mappers.OrderMapper;
import at.qe.skeleton.model.*;
import at.qe.skeleton.repositories.OrderRepository;
import at.qe.skeleton.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartValidationService cartValidationService;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public OrderDTO placeOrder(Userx user, CartDTO rawCart) {
        CartDTO validatedCart = cartValidationService.validateCart(rawCart)
                .orElseThrow(() -> new RuntimeException("Cart validation failed"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROGRESS);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItemDTO itemDto : validatedCart.items()) {
            Product product = productRepository.findById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.productId()));

            if (product.getStock() < itemDto.amount()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.amount());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.amount());
            orderItem.setPriceAtPurchase(product.getPrice());
            orderItem.setDiscountAtPurchase(product.getDiscount());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            total += (product.getPrice() * product.getDiscount() * itemDto.amount());
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);
        order.setStatus(OrderStatus.DONE);

        Order savedOrder = orderRepository.save(order);

        sendInvoiceEmail(savedOrder);

        return orderMapper.mapTo(savedOrder);
    }

    /**
     * Stubbed Method for sending an invoice email as plain-text.
     * Requirement: Architecture and logic should be correctly implemented as a stub.
     */
    private void sendInvoiceEmail(Order order) {
        String userEmail = order.getUser().getEmail();
        log.info("--------------------------------------------------");
        log.info("AUTOMATIC E-MAIL SYSTEM (STUB)");
        log.info("To: {}", userEmail);
        log.info("Subject: Your Order Confirmation & Invoice #{}", order.getId());
        log.info("Order Status: {}", order.getStatus());
        log.info("Total Amount: {} EUR", order.getTotalPrice());
        log.info("Details: Sent as plain-text invoice to customer.");
        log.info("--------------------------------------------------");
    }

    public OrderDTO getOrderById(Long id, Userx user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("Access denied: You are not allowed to view this order");
        }
        return orderMapper.mapTo(order);
    }

    public Collection<OrderDTO> getOrderHistory(Userx user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrder(Long id, Userx user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new RuntimeException("Access denied: You are not allowed to delete this order");
        }
        orderRepository.delete(order);
    }
}