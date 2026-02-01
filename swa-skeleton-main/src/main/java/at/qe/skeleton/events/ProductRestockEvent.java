package at.qe.skeleton.events;

import org.springframework.context.ApplicationEvent;

/**
 * Event published when a product is restocked.
 */
public class ProductRestockEvent extends ApplicationEvent {
    private final Long productId;

    public ProductRestockEvent(Object source, Long productId) {
        super(source);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
