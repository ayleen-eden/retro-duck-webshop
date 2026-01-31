package at.qe.skeleton.configs;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import at.qe.skeleton.services.ProductService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

/**
 * Data initializer for development and testing purposes.
 * <p>
 * This class is responsible for populating the database with initial test data
 * (primarily products) when the application starts. It is only active when the
 * {@code dev} Spring profile is used.
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    /**
     * Bean for initializing product data upon application startup.
     * <p>
     * The {@link ApplicationRunner} checks if the product database is empty.
     * If no products are present, it creates a set of predefined sample products.
     *
     * @param productService the service used to check for existing products and save new ones
     * @return an {@link ApplicationRunner} that executes the initialization logic
     */
    @Bean
    ApplicationRunner initData(ProductService productService) {
        return args -> {
            if (!productService.getAllProducts().isEmpty()) {
                return;
            }

            System.out.println("--- Initializing Test Data for Products ---");

            // QUAK - Dummy Product
            Product p1 = new Product();
            p1.setName("QUAK(e)");
            p1.setDescription("The groundbreaking first-person quacker that quacked a generation. Yellow, squishy, and QUAK.");
            p1.setPrice(9.99);
            p1.setStock(50L);
            p1.setDiscount(0.0);
            p1.setImageUrl("/images/quak(e).png");
            p1.setCategories(Set.of(ProductCategory.PC));
            productService.saveProduct(p1);

            createProduct(productService, "Panzer Dragoon Saga", "Enter the world of Panzer Dragoon Saga and experience a game like no other: a fusion of classic Panzer action with the most technologically advanced RPG to come to Saturn.", 999.99, 2, 0.2, ProductCategory.SATURN, "https://upload.wikimedia.org/wikipedia/en/6/64/PanzerDragoonSagaBox.jpg");
            createProduct(productService, "Dead Space", "Only the Dead Survive.", 9.99, 2, 0.5, ProductCategory.XBOX_360, "https://upload.wikimedia.org/wikipedia/en/5/57/Dead_Space_Box_Art.jpg");

            System.out.println("--- Test Data Initialized ---");
        };
    }

    /**
     * Helper method to simplify the creation and persistence of product entities.
     *
     * @param productService the service used to persist the product
     * @param name           the display name of the product
     * @param description    a detailed text description of the product
     * @param price          the base price of the product
     * @param stock          the initial inventory count
     * @param discount       the discount rate applied to the product (e.g., 0.2 for 20%)
     * @param category       the primary platform/category for the product
     * @param imageUrl       the URL string for the product's image
     */
    private void createProduct(ProductService productService, String name, String description, double price, long stock, double discount, ProductCategory category, String imageUrl) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(description);
        p.setPrice(price);
        p.setStock(stock);
        p.setDiscount(discount);
        p.setCategories(Set.of(category));
        p.setImageUrl(imageUrl);
        productService.saveProduct(p);
    }
}