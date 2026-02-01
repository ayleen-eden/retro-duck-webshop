package at.qe.skeleton.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a requested order cannot be found in the database.
 * <p>
 * This exception typically results in a {@code 404 Not Found} HTTP response.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends Exception {
    /**
     * Constructs a new OrderNotFoundException with the specified detail message.
     * * @param message the detail message (usually containing the order ID).
     */
    public OrderNotFoundException(String message) {
        super(message);
    }
}
