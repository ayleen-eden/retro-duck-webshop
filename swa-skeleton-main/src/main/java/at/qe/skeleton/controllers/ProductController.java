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

/**
 * REST Controller for product management.
 * <p>
 * Provides API endpoints for public product browsing as well as
 * restricted administrative tasks like creating, editing, or deleting products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final ProductCreateMapper productCreateMapper;

    /**
     * Constructor for dependency injection.
     * * @param productService service for business logic
     *
     * @param productMapper       mapper for product entities to DTOs
     * @param productCreateMapper mapper for creation DTOs to entities
     */
    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper, ProductCreateMapper productCreateMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.productCreateMapper = productCreateMapper;
    }

    // ===== GET =====

    /**
     * Retrieves all products available in the shop.
     * Accessible by everyone.
     * * @return a collection of all products as {@link ProductDTO}s
     */
    @GetMapping("/")
    public ResponseEntity<Collection<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts().stream().map(productMapper::mapTo).collect(Collectors.toList()));
    }

    /**
     * Retrieves a single product by its unique ID.
     * Accessible by everyone.
     * * @param id the unique ID of the product
     *
     * @return the found product as {@link ProductDTO}
     * @throws ResponseStatusException if the product is not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return ResponseEntity.ok(productMapper.mapTo(product));
    }

    /**
     * Retrieves all possible product categories defined in the system.
     * Accessible by everyone.
     * * @return an array of all {@link ProductCategory} values
     */
    @GetMapping("/categories")
    public ResponseEntity<ProductCategory[]> getCategories() {
        return ResponseEntity.ok(ProductCategory.values());
    }


    // ===== POST =====

    /**
     * Creates a new product record.
     * Restricted to users with administrative roles (ADMIN, MANAGER).
     * * @param productCreateDTO the data transfer object for the new product
     *
     * @return the created product as {@link ProductDTO} with status 201
     */
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product product = productCreateMapper.mapFrom(productCreateDTO);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.mapTo(savedProduct));
    }


    // ===== PATCH =====

    /**
     * Partially updates an existing product.
     * Restricted to users with administrative roles (ADMIN, MANAGER).
     * * @param id the ID of the product to update
     *
     * @param productDTO the updated fields
     * @return the updated product as {@link ProductDTO}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }


    // ===== DELETE =====

    /**
     * Deletes a specific product from the system.
     * Restricted to users with administrative roles (ADMIN, MANAGER).
     * * @param id the ID of the product to delete
     *
     * @throws ResponseStatusException if the product does not exist
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        if (productService.getProductById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productService.deleteProductById(id);
    }
}