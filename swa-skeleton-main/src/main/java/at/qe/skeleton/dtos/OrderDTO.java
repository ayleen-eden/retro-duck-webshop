package at.qe.skeleton.dtos;

import at.qe.skeleton.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

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