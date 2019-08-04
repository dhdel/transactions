package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public class IllegalOperationException extends DomainException {

    public IllegalOperationException(String message) {

        super(HttpStatus.BAD_REQUEST, message);
    }

    public IllegalOperationException(String message, Throwable throwable) {

        super(HttpStatus.BAD_REQUEST, message, throwable);
    }

}
