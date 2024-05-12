package cz.cvut.fit.sabirdan.wework.config.web;

import cz.cvut.fit.sabirdan.wework.http.exception.HttpException;
import cz.cvut.fit.sabirdan.wework.http.response.AttributeErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpException.class)
    public ResponseEntity<AttributeErrorResponse> authenticationExceptionToInvalidPassword(HttpException httpException) {
        return ResponseEntity
                .status(httpException.getStatusCode())
                .body(new AttributeErrorResponse(
                        httpException.getAttribute(),
                        httpException.getMessage()
                ));
    }
}