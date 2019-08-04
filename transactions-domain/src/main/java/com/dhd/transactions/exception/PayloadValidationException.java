package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public class PayloadValidationException extends DomainException {

    private static final long serialVersionUID = 1404521133003602656L;

    public PayloadValidationException(String message) {

        super(HttpStatus.BAD_REQUEST, message);
    }

    public PayloadValidationException(String message, Throwable throwable) {

        super(HttpStatus.BAD_REQUEST, message, throwable);
    }

}
