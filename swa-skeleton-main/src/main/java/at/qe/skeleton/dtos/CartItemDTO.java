package at.qe.skeleton.dtos;

public record CartItemDTO(
    Long productId,
    Double pricePerUnit,
    Integer amount
) {}
