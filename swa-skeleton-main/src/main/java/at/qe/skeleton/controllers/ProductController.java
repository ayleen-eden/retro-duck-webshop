package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.mappers.ProductCreateMapper;
import at.qe.skeleton.mappers.ProductMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import at.qe.skeleton.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductCreateMapper productCreateMapper;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, ProductCreateMapper productCreateMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.productCreateMapper = productCreateMapper;
    }

    // ===== GET =====

    // Load all products (allowed: everyone)
    @GetMapping("/")
    public ResponseEntity<Collection<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts().stream().map(productMapper::mapTo).collect(Collectors.toList()));
    }

    // Load single product (allowed: everyone)
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return ResponseEntity.ok(productMapper.mapTo(product));
    }

    // Load all categories (allowed: everyone)
    @GetMapping("/categories")
    public ResponseEntity<ProductCategory[]> getCategories() {
        return ResponseEntity.ok(ProductCategory.values());
    }


    // ===== POST =====

    // Create product (allowed: admin, manager)
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product product = productCreateMapper.mapFrom(productCreateDTO);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(productMapper.mapTo(savedProduct));
    }


    // ===== PATCH =====

    // Edit product (allowed: admin, manager)
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
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