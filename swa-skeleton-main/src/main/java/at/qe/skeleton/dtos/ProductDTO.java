package at.qe.skeleton.dtos;

import at.qe.skeleton.model.ProductCategory;
import java.util.Set;

/**
 * Data Transfer Object representing a product.
 * <p>
 * This record is used to transfer product data between the backend and frontend.
 * It provides a read-only snapshot of a product's state, including its identity,
 * pricing, availability, and classification.
 *
 * @param id          The unique identifier of the product.
 * @param name        The display name of the product.
 * @param description A detailed text describing the product.
 * @param price       The base price of the product.
 * @param stock       The current quantity available in inventory.
 * @param discount    The active discount rate applied to the product (e.g., 0.1 for 10%).
 * @param imageUrl    The URL or relative path to the product's image.
 * @param categories  A set of {@link ProductCategory} enums associated with this product.
 */
public record ProductDTO (
    Long id,
    String name,
    String description,
    Double price,
    Long stock,
    Double discount,
    String imageUrl,
    Set<ProductCategory> categories
) {}
