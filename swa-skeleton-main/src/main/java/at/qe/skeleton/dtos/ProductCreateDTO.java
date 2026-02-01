package at.qe.skeleton.dtos;

import at.qe.skeleton.model.ProductCategory;

import java.util.Set;

/**
 * Data Transfer Object for creating a new product.
 * <p>
 * This record is used to capture the necessary information from the frontend
 * when a user with appropriate permissions (MANAGER or ADMIN) intends to
 * add a new product to the catalog. It does not include an ID, as the ID
 * is assigned by the persistence layer upon creation.
 *
 * @param name        The display name of the new product.
 * @param description A text describing the product's features and details.
 * @param price       The initial base price of the product.
 * @param stock       The initial quantity to be added to the inventory.
 * @param discount    The initial discount rate (e.g., 0.0 for no discount).
 * @param imageUrl    The URL or path to the image representing the product.
 * @param categories  A set of {@link ProductCategory} enums to classify the product.
 */
public record ProductCreateDTO (
    String name,
    String description,
    Double price,
    Long stock,
    Double discount,
    String imageUrl,
    Set<ProductCategory> categories
) {}
