package at.qe.skeleton.events;

import org.springframework.context.ApplicationEvent;

/**
 * Event published when a product becomes out of stock.
 */
public class ProductOutOfStockEvent extends ApplicationEvent {
    private final Long productId;

    public ProductOutOfStockEvent(Object source, Long productId) {
        super(source);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
