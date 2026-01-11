package at.qe.skeleton.dtos;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public class ProductCreateDTO {
    // ===== Attributes =====

    @NotBlank(message = "Name can't be empty")
    private String name;

    @Size(max = 1000, message = "Description is too long")
    private String description;

    @PositiveOrZero(message = "Price can't be negative")
    private double price;

    @PositiveOrZero(message = "Stock can't be negative")
    private long stock;

    @PositiveOrZero(message = "Discount can't be negative")
    private double discount;

    private String imageUrl;

    private Set<ProductCategory> categories = new HashSet<>();


    // ===== Constructors =====

    public ProductCreateDTO() {
    }

    public ProductCreateDTO(String name, double price, long stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Product toEntity() {
        Product product = new Product();
        product.setName(this.name);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setStock(this.stock);
        product.setDiscount(this.discount);
        product.setImageUrl(this.imageUrl);
        product.setCategories(this.categories);
        return product;
    }


    // ===== Getter/Setter =====

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public long getStock() { return stock; }
    public void setStock(long stock) { this.stock = stock; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Set<ProductCategory> getCategories() { return categories; }
    public void setCategories(Set<ProductCategory> categories) { this.categories = categories; }
}