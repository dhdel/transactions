package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {

    public NotFoundException(String message) {

        super(HttpStatus.NOT_FOUND, message);
    }

    public NotFoundException(String message, Throwable throwable) {

        super(HttpStatus.NOT_FOUND, message, throwable);
    }

}
