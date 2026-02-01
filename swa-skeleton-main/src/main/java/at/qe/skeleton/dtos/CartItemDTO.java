package at.qe.skeleton.dtos;

/**
 * Data transfer object for validating localStorage cart in frontend.
 */
public record CartItemDTO(
    Long productId,
    String productName,
    String productImage,
    Double pricePerUnit,
    Integer amount
) {}
