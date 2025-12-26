package at.qe.skeleton.dtos;

public record CartItemDTO(
    Long productId,
    String productName,
    String productImage,
    Double pricePerUnit,
    Integer amount
) {}
