package at.qe.skeleton.dtos;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.RatingScale;
import at.qe.skeleton.model.Userx;

import java.time.LocalDateTime;

public record RatingDTO (
    Long id,
    LocalDateTime timestamp,
    RatingScale rating,
    String comment,
    Userx author,
    Product product
) {}
