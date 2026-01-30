package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.dtos.CheckoutRequestDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.exceptions.InsufficientStockException;
import at.qe.skeleton.exceptions.OrderNotFoundException;
import at.qe.skeleton.exceptions.UnauthorizedOrderAccessException;
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

    @Transactional(rollbackFor = Exception.class)
    public OrderDTO placeOrder(Userx user, CheckoutRequestDTO request) throws InsufficientStockException {
        CartDTO validatedCart = cartValidationService.validateCart(request.cart())
                .orElseThrow(() -> new RuntimeException("Cart validation failed"));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.IN_PROGRESS);

        order.setShippingName(request.shippingName());
        order.setShippingStreet(request.shippingStreet());
        order.setShippingCity(request.shippingCity());
        order.setShippingPostalCode(request.shippingPostalCode());
        order.setShippingCountry(request.shippingCountry());
        order.setPaymentMethod(request.paymentMethod());

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItemDTO itemDto : validatedCart.items()) {
            Product product = productRepository.findByIdWithLock(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemDto.amount()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.amount());
            productRepository.save(product);

            double priceAtPurchase = product.getPrice();
            double discount = product.getDiscount();
            double finalPricePerUnit = priceAtPurchase * (1 - discount);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.amount());
            orderItem.setPriceAtPurchase(finalPricePerUnit);
            orderItem.setDiscountAtPurchase(discount);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
            total += (finalPricePerUnit * itemDto.amount());
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
        log.info("Items:");
        for (OrderItem item : order.getItems()) {
            log.info(String.format("  - %s | Quantity: %d | Unit Price: %.2f EUR | Subtotal: %.2f EUR",
                    item.getProduct().getName(),
                    item.getQuantity(),
                    item.getPriceAtPurchase(),
                    (item.getQuantity() * item.getPriceAtPurchase())
            ));
        }
        log.info(String.format("Total Amount: %.2f EUR", order.getTotalPrice()));
        log.info("Details: Sent as plain-text invoice to customer.");
        log.info("--------------------------------------------------");
    }

    public OrderDTO getOrderById(Long id, Userx user) throws OrderNotFoundException, UnauthorizedOrderAccessException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new UnauthorizedOrderAccessException("Access denied for order: " + id);
        }
        return orderMapper.mapTo(order);
    }

    public Collection<OrderDTO> getOrderHistory(Userx user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
    }

    public void deleteOrder(Long id, Userx user) throws OrderNotFoundException, UnauthorizedOrderAccessException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new UnauthorizedOrderAccessException("Access denied for order: " + id);
        }
        orderRepository.delete(order);
    }
}