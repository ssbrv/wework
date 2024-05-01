package cz.cvut.fit.sabirdan.wework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends ResponseStatusException {
    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
