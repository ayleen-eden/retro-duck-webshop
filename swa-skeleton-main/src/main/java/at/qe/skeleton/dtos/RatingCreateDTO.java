package at.qe.skeleton.dtos;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.RatingScale;
import at.qe.skeleton.model.Userx;
import jakarta.validation.constraints.NotBlank;

public record RatingCreateDTO(
        @NotBlank
        RatingScale rating,
        @NotBlank
        String comment,
        @NotBlank
        Userx author,
        @NotBlank
        Product product
) {}
