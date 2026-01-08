package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.events.ProductOutOfStockEvent;
import at.qe.skeleton.events.ProductRestockEvent;
import at.qe.skeleton.events.ProductSaleEvent;
import at.qe.skeleton.mappers.ProductMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ApplicationEventPublisher publisher;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductService productService, ApplicationEventPublisher publisher, ProductMapper productMapper) {
        this.productService = productService;
        this.publisher = publisher;
        this.productMapper = productMapper;
    }

    // ===== GET =====

    // Load all products (allowed: everyone)
    @GetMapping("/")
    public Collection<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(productMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    // Load single product (allowed: everyone)
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return productMapper.mapToDTO(product);
    }


    // ===== POST =====

    // Create product (allowed: admin, manager)
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product product = productMapper.mapToEntity(productCreateDTO);
        Product savedProduct = productService.saveProduct(product);
        return productMapper.mapToDTO(savedProduct);
    }


    // ===== PATCH =====

    // Edit product (allowed: admin, manager)
    @PatchMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        if (productDTO.name() != null) existingProduct.setName(productDTO.name());
        if (productDTO.description() != null) existingProduct.setDescription(productDTO.description());
        if (productDTO.price() != null && productDTO.price() >= 0) existingProduct.setPrice(productDTO.price());
        if (productDTO.stock() != null && productDTO.stock() >= 0) {
            if (productDTO.stock() > existingProduct.getStock()) {
                publisher.publishEvent(
                        new ProductRestockEvent(this, existingProduct.getId())
                );
            }

            if (productDTO.stock() == 0) {
                publisher.publishEvent(
                        new ProductOutOfStockEvent(this, existingProduct.getId())
                );
            }

            existingProduct.setStock(productDTO.stock());
        }

        if (productDTO.discount() != null) {
            if (productDTO.discount() > existingProduct.getDiscount()) {
                publisher.publishEvent(
                    new ProductSaleEvent(this, existingProduct.getId())
                );
            }

            existingProduct.setDiscount(productDTO.discount());
        }

        Product updatedProduct = productService.saveProduct(existingProduct);
        return productMapper.mapToDTO(updatedProduct);
    }


    // ===== DELETE =====

    // Delete product (allowed: admin, manager)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productService.deleteProductById(id);
    }
}