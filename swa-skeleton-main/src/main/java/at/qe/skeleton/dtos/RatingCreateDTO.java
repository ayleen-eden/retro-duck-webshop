package at.qe.skeleton.dtos;

import at.qe.skeleton.model.RatingScale;
import jakarta.validation.constraints.NotBlank;

/**
 * Reduced data tranfer object for the RatingTypes Entity in the create endpoint.
 */
public record RatingCreateDTO(
        RatingScale rating,
        @NotBlank
        String comment,
        Long authorId,
        String username,
        Long productId
) {}
