// src/main/java/com/example/demo/exception/DefaultExceptionAdvice.java
package com.rookies4.MySpringBootLab.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusinessException(BusinessException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .statusCode(ex.getHttpStatus().value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorObject, ex.getHttpStatus());
    }
}