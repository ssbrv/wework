package cz.cvut.fit.sabirdan.wework.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class ConflictException extends HttpException {
    public ConflictException(@Nullable String attribute, @Nullable String message) {
        super(HttpStatus.CONFLICT, attribute, message);
    }
    public ConflictException(@Nullable String message) {
        this(null, message);
    }
    public ConflictException() {
        this(null, null);
    }

}
