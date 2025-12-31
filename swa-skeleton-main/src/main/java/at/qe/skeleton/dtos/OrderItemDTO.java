package at.qe.skeleton.dtos;

public record OrderItemDTO(
        Long productId,
        String productName,
        int quantity,
        double priceAtPurchase,
        double discountAtPurchase
) {}
