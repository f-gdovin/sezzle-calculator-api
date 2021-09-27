package dev.gdovin.sezzle.calculator.api;

import dev.gdovin.sezzle.calculator.exception.AbstractCalculatingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvisor {

    private static final String INTERNAL_ERROR_DESC = "Unexpected error occurred, please see server logs.";

    @ExceptionHandler({ AbstractCalculatingException.class })
    public ResponseEntity<ApiError> handleUserError(AbstractCalculatingException thrown) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, thrown.getMessage());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ApiError> handleUserError(Exception thrown) {
        log.warn("Uncaught exception " + thrown);
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_ERROR_DESC);
    }

    private static ResponseEntity<ApiError> buildResponseEntity(HttpStatus codeToThrow, String message) {
        return new ResponseEntity<>(new ApiError(message), codeToThrow);
    }
}
