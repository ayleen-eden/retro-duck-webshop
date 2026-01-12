package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements DTOMapper<Product, ProductDTO> {

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