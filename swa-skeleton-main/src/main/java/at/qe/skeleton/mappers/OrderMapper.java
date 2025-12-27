package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.dtos.OrderItemDTO;
import at.qe.skeleton.model.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDTO toDto(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getItems().stream().map(item -> new OrderItemDTO(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getDiscountAtPurchase()
                )).collect(Collectors.toList())
        );
    }
}