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

/**
 * Unit tests for {@link ProductService}.
 * <p>
 * This class uses Mockito to mock the {@link ProductRepository} dependency,
 * ensuring that the service logic is tested in isolation from the database.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    /**
     * Mocked repository to simulate database interactions.
     */
    @Mock
    private ProductRepository productRepository;

    /**
     * The service under test, with mocked dependencies injected.
     */
    @InjectMocks
    private ProductService productService;


    // ===== Tests =====

    /**
     * Tests the retrieval of all products.
     * <p>
     * Verifies that the service correctly delegates the call to the repository
     * and returns the expected list of products.
     */
    @Test
    public void testGetAllProducts() {
        Product p1 = new Product();
        p1.setName("My Product");
        Mockito.when(productRepository.findAll()).thenReturn(List.of(p1));

        var products = productService.getAllProducts();

        Assertions.assertEquals(1, products.size());
        Assertions.assertEquals("My Product", products.iterator().next().getName());
    }

    /**
     * Tests finding a product by its unique ID.
     * <p>
     * Verifies that the service returns an {@link Optional} containing the
     * product when the repository finds it.
     */
    @Test
    public void testGetProductById_Found() {
        Product p1 = new Product();
        p1.setId(1L);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Product> result = productService.getProductById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(1L, result.get().getId());
    }

    /**
     * Tests saving a product entity.
     * <p>
     * Verifies that the service passes the product to the repository and
     * returns the saved entity. Also ensures the repository's save method
     * was called exactly once.
     */
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