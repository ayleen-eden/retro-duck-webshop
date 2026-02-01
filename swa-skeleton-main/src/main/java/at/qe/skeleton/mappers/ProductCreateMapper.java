package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.model.Product;
import org.springframework.stereotype.Component;

/**
 * Mapper component responsible for converting between {@link Product} entities
 * and {@link ProductCreateDTO}s.
 * <p>
 * This specific mapper is tailored for the product creation workflow, ensuring
 * that data received from the frontend via a {@link ProductCreateDTO} is
 * correctly transformed into a persistent {@link Product} entity.
 */
@Component
public class ProductCreateMapper implements DTOMapper<Product, ProductCreateDTO> {

    /**
     * Maps a {@link Product} entity to a {@link ProductCreateDTO}.
     * <p>
     * Useful for mirroring back the creation data or for testing purposes.
     *
     * @param product the persistent product entity.
     * @return a data transfer object containing the product's details.
     */
    @Override
    public ProductCreateDTO mapTo(Product product) {
        return new ProductCreateDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getDiscount(),
                product.getImageUrl(),
                product.getCategories()
        );
    }

    /**
     * Maps a {@link ProductCreateDTO} to a new {@link Product} entity.
     * <p>
     * This method initializes a fresh product entity with the details provided
     * in the DTO. The ID remains unassigned as it is expected to be generated
     * by the database upon persistence.
     *
     * @param dto the data transfer object received during product creation.
     * @return a new product entity populated with values from the DTO.
     */
    @Override
    public Product mapFrom(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());
        product.setDiscount(dto.discount());
        product.setImageUrl(dto.imageUrl());
        product.setCategories(dto.categories());
        return product;
    }
}