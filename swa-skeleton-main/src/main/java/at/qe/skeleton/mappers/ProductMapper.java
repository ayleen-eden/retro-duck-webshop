package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.model.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between {@link Product} entities and {@link ProductDTO}s.
 * <p>
 * This class provides logic to transform persistent database entities into
 * data transfer objects for the frontend and vice versa, ensuring a clean
 * separation between the API and the data model.
 */
@Component
public class ProductMapper implements DTOMapper<Product, ProductDTO> {

    /**
     * Maps a {@link ProductDTO} to a {@link Product} entity.
     * <p>
     * Note: Since the ID is typically managed by the database during creation,
     * it is not manually set here unless specifically required by the use case.
     *
     * @param dto the data transfer object containing product details.
     * @return a new product entity populated with data from the DTO.
     */
    @Override
    public Product mapFrom(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.name()); // Bei Records: .name() statt .getName()
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setDiscount(dto.discount());
        product.setImageUrl(dto.imageUrl());
        product.setCategories(dto.categories());
        return product;
    }

    /**
     * Maps a {@link Product} entity to a {@link ProductDTO}.
     * <p>
     * This creates a snapshot of the product's current state suitable for
     * transmission over the network.
     *
     * @param product the persistent entity to map from.
     * @return a data transfer object representing the product.
     */
    @Override
    public ProductDTO mapTo(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getDiscount(),
                product.getImageUrl(),
                product.getCategories()
        );
    }
}