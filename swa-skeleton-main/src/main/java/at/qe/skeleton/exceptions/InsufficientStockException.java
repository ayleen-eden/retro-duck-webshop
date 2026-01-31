package at.qe.skeleton.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when an order cannot be processed because the requested
 * quantity of a product exceeds the current stock level.
 * <p>
 * This is a checked exception that results in a {@code 409 Conflict}
 * status when handled by the REST layer.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends Exception {
    /**
     * Constructs a new InsufficientStockException with the specified detail message.
     * * @param message the detail message explaining which product is out of stock.
     */
    public InsufficientStockException(String message) {
        super(message);
    }
}