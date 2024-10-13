package com.prem.userservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(getClass().getName());

    @ExceptionHandler({InvalidUsernameOrPassword.class, UserAlreadyExistsException.class})
    public ResponseEntity<Object> handleInvalidUsernameOrPassword(InvalidUsernameOrPassword e) {
        logger.info("InvalidUsernameOrPassword occurred: " + e.getMessage());
        return new ResponseEntity<>(e.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public String handleTokenExpiredException(TokenExpiredException e) {
        logger.info("TokenExpiredException occurred: " + e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    public String finalExceptionHandler(TokenExpiredException e) {
        logger.info("Main exception handler: " + e.getMessage());
        return e.getMessage();
    }
}
