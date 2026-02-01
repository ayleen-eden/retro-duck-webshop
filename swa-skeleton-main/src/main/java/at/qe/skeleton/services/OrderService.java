package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.dtos.CheckoutRequestDTO;
import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.exceptions.InsufficientStockException;
import at.qe.skeleton.exceptions.OrderNotFoundException;
import at.qe.skeleton.exceptions.UnauthorizedOrderAccessException;
import at.qe.skeleton.mappers.OrderMapper;
import at.qe.skeleton.mappers.ProductMapper;
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

/**
 * Service class for managing the lifecycle of orders.
 * <p>
 * This service handles the core business logic for order processing, including
 * the checkout procedure, stock validation, history retrieval, and secure access
 * to individual order details.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartValidationService cartValidationService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    /**
     * Processes a checkout request by creating a persistent order for a user.
     * <p>
     * This operation validates the current cart state, checks for sufficient product stock,
     * updates stock levels in the warehouse, and calculates the total price based on
     * snapshots of current prices and discounts. The operation is transactional to
     * ensure data consistency between order creation and stock decrement.
     *
     * @param user            the authenticated user placing the order.
     * @param request the DTO containing shipping address and payment details.
     * @return an {@link OrderDTO} representation of the newly created order.
     * @throws InsufficientStockException if any product quantity in the cart exceeds available stock.
     */
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
            Product product = productService.getProductById(itemDto.productId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < itemDto.amount()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - itemDto.amount());
            productService.updateProduct(product.getId(), productMapper.mapTo(product));
            productService.saveProduct(product);

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

    /**
     * Retrieves a specific order by its ID, verifying ownership for security.
     *
     * @param id   the unique identifier of the order.
     * @param user the authenticated user requesting the order data.
     * @return the mapped {@link OrderDTO}.
     * @throws OrderNotFoundException           if the order does not exist in the database.
     * @throws UnauthorizedOrderAccessException if the requested order does not belong to the user.
     */
    public OrderDTO getOrderById(Long id, Userx user) throws OrderNotFoundException, UnauthorizedOrderAccessException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new UnauthorizedOrderAccessException("Access denied for order: " + id);
        }
        return orderMapper.mapTo(order);
    }

    /**
     * Retrieves the complete order history for a specific user (used to display in the frontend).
     *
     * @param user the user whose order history is being requested.
     * @return a collection of {@link OrderDTO} objects.
     */
    public Collection<OrderDTO> getOrderHistory(Userx user) {
        return orderRepository.findByUser(user)
                .stream()
                .map(orderMapper::mapTo)
                .collect(Collectors.toList());
    }

    /**
     * Deletes an order from the system after verifying existence and ownership.
     * Note: For simplicity we decided, that the order can only be deleted by the user himself, and not by any managers or admins.
     * But orders get deleted automatically if the user is deleted, so there is no need for any other users to access it.
     *
     * @param id   the unique identifier of the order to delete.
     * @param user the authenticated user attempting the deletion.
     * @throws OrderNotFoundException           if the order does not exist.
     * @throws UnauthorizedOrderAccessException if the user is not authorized to delete this specific order.
     */
    public void deleteOrder(Long id, Userx user) throws OrderNotFoundException, UnauthorizedOrderAccessException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));

        if (!order.getUser().equals(user)) {
            throw new UnauthorizedOrderAccessException("Access denied for order: " + id);
        }
        orderRepository.delete(order);
    }
}