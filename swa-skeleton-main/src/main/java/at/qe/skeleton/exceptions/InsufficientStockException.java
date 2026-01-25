package at.qe.skeleton.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InsufficientStockException extends Exception {
    public InsufficientStockException(String message) {
        super(message);
    }
}