package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Product} entities.
 * <p>
 * This interface provides standard CRUD operations through {@link JpaRepository}
 * as well as custom query methods for searching products by name, price, or category.
 * It also includes specialized methods for database-level locking.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds products whose names contain the specified string, ignoring case sensitivity.
     *
     * @param name the string to search for within product names.
     * @return a list of products matching the criteria.
     */
    List<Product> findByNameContainingIgnoreCase(String name);

    /**
     * Finds products with a price strictly less than the specified value.
     *
     * @param price the upper price limit (exclusive).
     * @return a list of products within the price range.
     */
    List<Product> findByPriceLessThan(double price);

    /**
     * Finds products associated with a specific {@link ProductCategory}.
     * <p>
     * Performs a join with the categories collection to filter products.
     *
     * @param category the category to filter by.
     * @return a list of products belonging to the given category.
     */
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findByCategory(@Param("category") ProductCategory category);

    /**
     * Retrieves a product by its ID and applies a pessimistic write lock.
     * <p>
     * This is useful during the checkout process to prevent race conditions
     * when updating stock levels.
     *
     * @param id the unique ID of the product.
     * @return an Optional containing the locked product, or empty if not found.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}