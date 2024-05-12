package cz.cvut.fit.sabirdan.wework.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends HttpException {
    public NotFoundException(@Nullable String attribute, @Nullable String message) {
        super(HttpStatus.NOT_FOUND, attribute, message);
    }
    public NotFoundException(@Nullable String message) {
        this(null, message);
    }
    public NotFoundException() {
        this(null, null);
    }
}
