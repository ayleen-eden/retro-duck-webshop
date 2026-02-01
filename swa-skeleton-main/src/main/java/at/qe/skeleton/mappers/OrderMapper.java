package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.OrderDTO;
import at.qe.skeleton.dtos.OrderItemDTO;
import at.qe.skeleton.model.Order;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Component responsible for mapping between {@link Order} entities and {@link OrderDTO}s.
 */
@Component
public class OrderMapper implements DTOMapper<Order, OrderDTO> {

    /**
     * Maps an {@link Order} entity to an {@link OrderDTO}.
     * <p>
     * This method transforms the persistent order state, including a calculated
     * list of {@link OrderItemDTO}s which capture the price and product
     * information at the time of purchase.
     *
     * @param order the order entity to map.
     * @return a new {@link OrderDTO} containing the mapped data, or {@code null} if the input is null.
     */
    @Override
    public OrderDTO mapTo(Order order) {
        if (order == null) {
            return null;
        }
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
                )).collect(Collectors.toList()),
                order.getShippingName(),
                order.getPaymentMethod()
        );
    }

    /**
     * Maps an {@link OrderDTO} back to an {@link Order} entity.
     * <p>
     * Note: This method is implemented to fulfill the {@link DTOMapper} interface
     * contract. However, in the current architecture, mapping from a DTO back
     * to a full Order entity is not intended to be used, as orders are
     * managed through dedicated service logic.
     *
     * @param dto the data transfer object to map from.
     * @return a new {@link Order} entity with only the ID preserved from the DTO.
     */
    @Override
    public Order mapFrom(OrderDTO dto) {
        if (dto == null) return null;
        Order order = new Order();
        order.setId(dto.id());
        return order;
    }
}