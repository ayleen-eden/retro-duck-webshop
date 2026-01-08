package at.qe.skeleton.dtos;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.ProductCategory;
import java.util.Set;
import java.util.HashSet;
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private long stock;
    private double discount;
    private String imageUrl;
    private Set<ProductCategory> categories;

    // Empty constructor
    public ProductDTO() {}

    // Constructor from entity 'product'
    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.discount = product.getDiscount();
        this.imageUrl = product.getImageUrl();
        this.categories = product.getCategories() != null ? product.getCategories() : new HashSet<>();
    }

    // ===== Getter/Setter =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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