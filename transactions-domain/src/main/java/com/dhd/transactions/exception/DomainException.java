package com.dhd.transactions.exception;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {

    private final HttpStatus status;

    public DomainException(HttpStatus status, String message) {

        super(message);
        this.status = status;
    }

    public DomainException(HttpStatus status, String message, Throwable cause) {

        super(message, cause);
        this.status = status;
    }

    /**
     * @return the status
     */
    public HttpStatus getStatus() {

        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "DomainException [status=" + status + ", getMessage()=" + getMessage() + ", getCause()=" + getCause()
                + "]";
    }

}
