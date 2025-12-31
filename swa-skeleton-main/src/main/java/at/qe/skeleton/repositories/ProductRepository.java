package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceLessThan(double price);

    // SQL: Find product by category
    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findByCategory(@Param("category") ProductCategory category);
}