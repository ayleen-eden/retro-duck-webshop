package at.qe.skeleton.events;

import org.springframework.context.ApplicationEvent;

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
