package at.qe.skeleton.services;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.repositories.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    // ===== Setup =====
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    // ===== Tests =====
    @Test
    public void testGetAllProducts() {
        Product p1 = new Product();
        p1.setName("My Product");
        Mockito.when(productRepository.findAll()).thenReturn(List.of(p1));

        var products = productService.getAllProducts();

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("My Product", products.iterator().next().getName());
    }

    @Test
    public void testGetProductById_Found() {
        Product p1 = new Product();
        p1.setId(1L);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Product> result = productService.getProductById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get().getId());
    }

    @Test
    public void testSaveProduct() {
        Product p1 = new Product();
        p1.setName("New Product");
        Mockito.when(productRepository.save(p1)).thenReturn(p1);

        Product saved = productService.saveProduct(p1);

        Assertions.assertNotNull(saved);
        Mockito.verify(productRepository, Mockito.times(1)).save(p1);
    }
}