package at.qe.skeleton.model;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entity representing a specific product position within an {@link Order}.
 * <p>
 * This class stores a snapshot of the product's price and discount at the time
 * of purchase to ensure historical accuracy even if product prices change later.
 */
@Entity
public class OrderItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    @Column(nullable = false)
    private Double priceAtPurchase;

    @Column(nullable = false)
    private Double discountAtPurchase;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public double getDiscountAtPurchase() { return discountAtPurchase; }
    public void setDiscountAtPurchase(double discountAtPurchase) { this.discountAtPurchase = discountAtPurchase; }
}