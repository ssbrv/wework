package cz.cvut.fit.sabirdan.wework.config.web;

import cz.cvut.fit.sabirdan.wework.http.exception.HttpException;
import cz.cvut.fit.sabirdan.wework.http.response.AttributeErrorResponse;
import cz.cvut.fit.sabirdan.wework.utils.GreatestStaysMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
}