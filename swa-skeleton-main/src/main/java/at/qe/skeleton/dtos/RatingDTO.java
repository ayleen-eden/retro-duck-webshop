package at.qe.skeleton.dtos;

import at.qe.skeleton.model.RatingScale;
import java.time.LocalDateTime;

/**
 * Data transfer object for the RatingTypes Entity.
 */
public record RatingDTO (
    Long id,
    LocalDateTime timestamp,
    RatingScale rating,
    String comment,
    Long authorId,
    String username,
    Long productId
) {}
