package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public class DatabaseOperationException extends DomainException {

    public DatabaseOperationException(String message) {

        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public DatabaseOperationException(String message, Throwable throwable) {

        super(HttpStatus.INTERNAL_SERVER_ERROR, message, throwable);
    }

}
