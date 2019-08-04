package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public class ParameterValidationException extends DomainException {

    private static final long serialVersionUID = 1404521133003602656L;

    public ParameterValidationException(String message) {

        super(HttpStatus.BAD_REQUEST, message);
    }

    public ParameterValidationException(String message, Throwable throwable) {

        super(HttpStatus.BAD_REQUEST, message, throwable);
    }

}
