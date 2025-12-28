package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.ProductCreateDTO;
import at.qe.skeleton.dtos.ProductDTO;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ===== GET =====

    // Load all products (allowed: everyone)
    @GetMapping("/")
    public Collection<ProductDTO> getAllProducts() {
        return productService.getAllProducts().stream()
                .map(ProductDTO::new)
                .collect(Collectors.toList());
    }

    // Load single product (allowed: everyone)
    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return new ProductDTO(product);
    }


    // ===== POST =====

    // Create product (allowed: admin, manager)
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        Product product = productCreateDTO.toEntity();
        Product savedProduct = productService.saveProduct(product);
        return new ProductDTO(savedProduct);
    }


    // ===== PATCH =====

    // Edit product (allowed: admin, manager)
    @PatchMapping("/{id}")
    public ProductDTO updateProduct(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        Product existingProduct = productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Simples Update Mapping
        if (productDTO.getName() != null) existingProduct.setName(productDTO.getName());
        if (productDTO.getDescription() != null) existingProduct.setDescription(productDTO.getDescription());
        if (productDTO.getPrice() >= 0) existingProduct.setPrice(productDTO.getPrice());
        if (productDTO.getStock() >= 0) existingProduct.setStock(productDTO.getStock());
        // TODO: Further mapping

        Product updatedProduct = productService.saveProduct(existingProduct);
        return new ProductDTO(updatedProduct);
    }


    // ===== DELETE =====

    // Delete product (allowed: admin, manager)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        if (!productService.getProductById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        productService.deleteProductById(id);
    }
}