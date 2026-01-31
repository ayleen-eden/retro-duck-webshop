package at.qe.skeleton.dtos;

/**
 * Data transfer object for the Subscription Entity.
 */
public record SubscriptionDTO(
    Long id,
    Long userId,
    Long productId
) {}
