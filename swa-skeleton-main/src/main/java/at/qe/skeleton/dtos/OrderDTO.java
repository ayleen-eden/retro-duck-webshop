package at.qe.skeleton.dtos;

import at.qe.skeleton.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object representing a summary of an order.
 * <p>
 * This record is used to securely transfer order data to the frontend
 * without exposing internal entity structures.
 *
 * @param id                the unique identifier of the order.
 * @param orderDate         the timestamp when the order was placed.
 * @param status            the current status of the order (e.g., {@link OrderStatus#NEW}).
 * @param totalPrice        the calculated total price of the order.
 * @param items             the list of {@link OrderItemDTO}s included in the order.
 * @param shippingName      the name of the recipient for shipping.
 * @param paymentMethod     the chosen payment method provider.
 */
public record OrderDTO(
        Long id,
        LocalDateTime orderDate,
        OrderStatus status,
        double totalPrice,
        List<OrderItemDTO> items,
        String shippingName,
        String paymentMethod
) {
}