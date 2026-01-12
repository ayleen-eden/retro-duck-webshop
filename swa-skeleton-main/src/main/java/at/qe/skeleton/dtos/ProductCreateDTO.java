package at.qe.skeleton.dtos;

import at.qe.skeleton.model.ProductCategory;

import java.util.Set;

public record ProductCreateDTO (
    String name,
    String description,
    Double price,
    Long stock,
    Double discount,
    String imageUrl,
    Set<ProductCategory> categories
) {}
