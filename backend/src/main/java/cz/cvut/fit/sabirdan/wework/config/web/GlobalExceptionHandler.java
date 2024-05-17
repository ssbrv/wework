package cz.cvut.fit.sabirdan.wework.config.web;

import cz.cvut.fit.sabirdan.wework.http.exception.HttpException;
import cz.cvut.fit.sabirdan.wework.http.response.AttributeErrorResponse;
import cz.cvut.fit.sabirdan.wework.utils.GreatestStaysMap;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<AttributeErrorResponse> provideAttributeError(HttpException httpException) {

        // do not handle the exception if the error is not in attribute
        if (httpException.getAttribute() == null)
            throw httpException;

        return ResponseEntity
                .status(httpException.getStatusCode())
                .body(new AttributeErrorResponse(
                        httpException.getAttribute(),
                        httpException.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AttributeErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        GreatestStaysMap<String, String> greatestStaysMap = new GreatestStaysMap<>();
        for (FieldError fieldError : fieldErrors)
            greatestStaysMap.put(fieldError.getField(), fieldError.getDefaultMessage());

        AttributeErrorResponse attributeErrorResponse = new AttributeErrorResponse();
        for (Map.Entry<String, String> fieldError : greatestStaysMap.getAll().entrySet())
            attributeErrorResponse.addAttributeError(fieldError.getKey(), fieldError.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(attributeErrorResponse);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "We cannot confirm your identity. Please, reauthenticate")
    public void handleInvalidToken(SignatureException ignore) {}

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "We cannot confirm your identity. Please, reauthenticate")
    public void handleInvalidToken(MalformedJwtException ignore) {}

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Your session has been expired. Please, reauthenticate")
    public void handleExpiredToken(ExpiredJwtException ignore) {}
}