package cz.cvut.fit.sabirdan.wework.http.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Getter
@Setter
public class HttpException extends ResponseStatusException {

    @Nullable
    private String attribute;

    public HttpException(HttpStatusCode httpStatusCode) {
        super(httpStatusCode);
    }

    public HttpException(HttpStatusCode httpStatusCode, @Nullable String message) {
        super(httpStatusCode, message);
    }

    public HttpException(HttpStatusCode httpStatusCode, @Nullable String attribute, @Nullable String message) {
        super(httpStatusCode, message);
        this.attribute = attribute;
    }

    @NonNull
    @Override
    public String getMessage() {
        return Objects.requireNonNull(super.getReason());
    }
}
