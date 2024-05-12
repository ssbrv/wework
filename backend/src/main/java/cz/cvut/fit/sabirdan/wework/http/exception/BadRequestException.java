package cz.cvut.fit.sabirdan.wework.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends HttpException {
    public BadRequestException(@Nullable String attribute, @Nullable String message) {
        super(HttpStatus.BAD_REQUEST, attribute, message);
    }
    public BadRequestException(@Nullable String message) {
        this(null, message);
    }
    public BadRequestException() {
        this(null, null);
    }
}
