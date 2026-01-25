package at.qe.skeleton.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedOrderAccessException extends Exception {
    public UnauthorizedOrderAccessException(String message) {
        super(message);
    }
}
