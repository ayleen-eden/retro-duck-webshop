package at.qe.skeleton.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a user attempts to access or modify an order
 * that does not belong to them.
 * <p>
 * This security-related exception results in a {@code 403 Forbidden} HTTP response.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedOrderAccessException extends Exception {
    /**
     * Constructs a new UnauthorizedOrderAccessException with the specified detail message.
     * * @param message the detail message explaining the access violation.
     */
    public UnauthorizedOrderAccessException(String message) {
        super(message);
    }
}
