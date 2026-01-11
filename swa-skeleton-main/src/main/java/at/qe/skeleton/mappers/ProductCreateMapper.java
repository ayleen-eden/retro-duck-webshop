package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductCreateMapper implements DTOMapper<Product, ProductCreateDTO> {

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
