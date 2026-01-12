package at.qe.skeleton.events;

import org.springframework.context.ApplicationEvent;

public class ProductSaleEvent extends ApplicationEvent {
    private final Long productId;

    public ProductSaleEvent(Object source, Long productId) {
        super(source);
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
