package at.qe.skeleton.services;

import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.events.ProductOutOfStockEvent;
import at.qe.skeleton.events.ProductRestockEvent;
import at.qe.skeleton.events.ProductSaleEvent;
import at.qe.skeleton.mappers.ProductMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Scope("application")
public class ProductService {

    private ProductRepository productRepository;

    private final ApplicationEventPublisher publisher;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ApplicationEventPublisher publisher, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.publisher = publisher;
        this.productMapper = productMapper;
    }

    // ===== READ =====

    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // ===== WRITE =====

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    public ProductDTO updateProduct(Long oldProductId, ProductDTO newProduct) {
        Product oldProduct = getProductById(oldProductId).orElse(null);

        if (oldProduct == null) {
            //TODO
            return null;
        }

        if (newProduct.name() != null) oldProduct.setName(newProduct.name());
        if (newProduct.description() != null) oldProduct.setDescription(newProduct.description());
        if (newProduct.price() != null) oldProduct.setPrice(newProduct.price());
        if (newProduct.imageUrl() != null) oldProduct.setImageUrl(newProduct.imageUrl());

        if (newProduct.stock() != null && newProduct.stock() >= 0) {
            if (newProduct.stock() > oldProduct.getStock()) {
                publisher.publishEvent(
                        new ProductRestockEvent(this, oldProduct.getId())
                );
            }

            if (newProduct.stock() == 0) {
                publisher.publishEvent(
                        new ProductOutOfStockEvent(this, oldProduct.getId())
                );
            }

            oldProduct.setStock(newProduct.stock());
        }

        if (newProduct.discount() != null) {
            if (newProduct.discount() > oldProduct.getDiscount()) {
                publisher.publishEvent(
                        new ProductSaleEvent(this, oldProduct.getId())
                );
            }

            oldProduct.setDiscount(newProduct.discount());
        }

        Product updatedProduct = saveProduct(oldProduct);
        return productMapper.mapTo(updatedProduct);
    }
}