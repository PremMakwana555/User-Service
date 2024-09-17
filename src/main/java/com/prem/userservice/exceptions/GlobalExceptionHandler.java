package com.prem.userservice.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = Logger.getLogger(getClass().getName());

    @ExceptionHandler(InvalidUsernameOrPassword.class)
    public String handleInvalidUsernameOrPassword(InvalidUsernameOrPassword e) {
        return e.getMessage();
    }
}
