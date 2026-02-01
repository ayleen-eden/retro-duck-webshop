package at.qe.skeleton.services;

import at.qe.skeleton.dtos.CartDTO;
import at.qe.skeleton.dtos.CartItemDTO;
import at.qe.skeleton.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@WithMockUser(username = "user1", authorities = {"CUSTOMER"})
public class CartValidationServiceTest {

    @Autowired
    private CartValidationService cartValidationService;

    @Autowired
    private ProductService productService;

    Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setDiscount(0.2);
        product.setStock(10L);
        product.setDescription("Test product");
        product.setImageUrl("img.jpg");

        productService.saveProduct(product);
    }

    @Test
    public void testValidationSuccess() {
        CartItemDTO item = new CartItemDTO(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                2
        );

        CartDTO cart = new CartDTO(List.of(item));

        Optional<CartDTO> result = cartValidationService.validateCart(cart);

        assertTrue(result.isPresent());
        assertEquals(80.0, result.get().items().iterator().next().pricePerUnit());
    }

    @Test
    public void testProductNotFound() {
        CartItemDTO item = new CartItemDTO(
                999L,
                "Nonexistent",
                "img.jpg",
                50.0,
                1
        );

        CartDTO cart = new CartDTO(List.of(item));

        Optional<CartDTO> result = cartValidationService.validateCart(cart);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testInsufficientStock() {
        CartItemDTO item = new CartItemDTO(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                20
        );

        CartDTO cart = new CartDTO(List.of(item));

        Optional<CartDTO> result = cartValidationService.validateCart(cart);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testPriceMismatch() {
        CartItemDTO item = new CartItemDTO(
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                50.0,
                2
        );

        CartDTO cart = new CartDTO(List.of(item));

        Optional<CartDTO> result = cartValidationService.validateCart(cart);

        assertTrue(result.isPresent());

        assertEquals(80.0, result.get().items().iterator().next().pricePerUnit());
    }

}
