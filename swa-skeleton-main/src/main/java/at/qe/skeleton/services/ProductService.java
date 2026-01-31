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

/**
 * Service for managing products and their lifecycle.
 * <p>
 * This service provides methods for retrieving, saving, and updating products.
 * It also handles the logic for triggering notification events when product
 * attributes like stock levels or discounts change.
 */
@Service
@Scope("application")
public class ProductService {

    /**
     * Repository for data access to {@link Product} entities.
     */
    private ProductRepository productRepository;

    /**
     * Publisher for Spring Application Events to notify other components of changes.
     */
    private final ApplicationEventPublisher publisher;

    /**
     * Mapper to convert between {@link Product} entities and {@link ProductDTO}s.
     */
    private final ProductMapper productMapper;

    /**
     * Constructor for Dependency Injection.
     *
     * @param productRepository the repository for product persistence
     * @param publisher the event publisher for system-wide notifications
     * @param productMapper the mapper for DTO conversions
     */
    @Autowired
    public ProductService(ProductRepository productRepository, ApplicationEventPublisher publisher, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.publisher = publisher;
        this.productMapper = productMapper;
    }

    // ===== READ =====

    /**
     * Retrieves all products currently stored in the system.
     *
     * @return a collection of all products
     */
    public Collection<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Finds a specific product by its unique identifier.
     *
     * @param id the ID of the product to find
     * @return an Optional containing the found product, or empty if not found
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // ===== WRITE =====

    /**
     * Persists a product entity in the database.
     *
     * @param product the product entity to save
     * @return the saved product entity
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Removes a product from the system by its ID.
     *
     * @param id the ID of the product to delete
     */
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Updates an existing product with new details and publishes events based on changes.
     * <p>
     * This method compares old and new values for stock and discount to trigger:
     * <ul>
     * <li>{@link ProductRestockEvent} if stock is increased</li>
     * <li>{@link ProductOutOfStockEvent} if stock reaches zero</li>
     * <li>{@link ProductSaleEvent} if the discount is increased</li>
     * </ul>
     *
     * @param oldProductId the ID of the product to be updated
     * @param newProduct the DTO containing the updated values
     * @return the updated product as DTO, or {@code null} if the product does not exist
     */
    public ProductDTO updateProduct(Long oldProductId, ProductDTO newProduct) {
        Product oldProduct = getProductById(oldProductId).orElse(null);

        if (oldProduct == null) {
            return null;
        }

        Long previousStock = oldProduct.getStock();
        Double previousDiscount = oldProduct.getDiscount();

        if (newProduct.name() != null) oldProduct.setName(newProduct.name());
        if (newProduct.description() != null) oldProduct.setDescription(newProduct.description());
        if (newProduct.price() != null) oldProduct.setPrice(newProduct.price());
        if (newProduct.imageUrl() != null) oldProduct.setImageUrl(newProduct.imageUrl());

        if (newProduct.stock() != null && newProduct.stock() >= 0) {
            oldProduct.setStock(newProduct.stock());
        }

        if (newProduct.discount() != null) {
            oldProduct.setDiscount(newProduct.discount());
        }

        Product updatedProduct = saveProduct(oldProduct);

        // Event logic: Triggered based on state changes
        if (newProduct.stock() != null && newProduct.stock() > previousStock) {
            publisher.publishEvent(new ProductRestockEvent(this, updatedProduct.getId()));
        }

        if (newProduct.stock() != null && newProduct.stock() == 0) {
            publisher.publishEvent(new ProductOutOfStockEvent(this, updatedProduct.getId()));
        }

        if (newProduct.discount() != null && newProduct.discount() > previousDiscount) {
            publisher.publishEvent(new ProductSaleEvent(this, updatedProduct.getId()));
        }

        return productMapper.mapTo(updatedProduct);
    }

}