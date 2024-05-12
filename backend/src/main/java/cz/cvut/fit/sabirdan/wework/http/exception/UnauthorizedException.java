package cz.cvut.fit.sabirdan.wework.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends HttpException {
    public UnauthorizedException(@Nullable String attribute, @Nullable String message) {
        super(HttpStatus.UNAUTHORIZED, attribute, message);
    }
    public UnauthorizedException(@Nullable String message) {
        this(null, message);
    }
    public UnauthorizedException() {
        this(null, null);
    }
}