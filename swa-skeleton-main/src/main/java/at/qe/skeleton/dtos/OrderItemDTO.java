package at.qe.skeleton.dtos;

/**
 * Data Transfer Object representing a single item within an order.
 * <p>
 * Contains details about the product, quantity, and financial conditions at the
 * time of purchase (snapshots of price and discount).
 *
 * @param productId          the unique identifier of the ordered product.
 * @param productName        the name of the product.
 * @param quantity           the number of units ordered.
 * @param priceAtPurchase    the unit price of the product at the time of ordering.
 * @param discountAtPurchase the discount applied to this item at the time of ordering.
 */
public record OrderItemDTO(
        Long productId,
        String productName,
        int quantity,
        double priceAtPurchase,
        double discountAtPurchase
) {}
