package at.qe.skeleton.dtos;

import at.qe.skeleton.model.RatingScale;

import java.time.LocalDateTime;

public record RatingDTO (
    Long id,
    LocalDateTime timestamp,
    RatingScale rating,
    String comment,
    Long authorId,
    Long productId
) {}
