package com.dhd.webflux.error;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.dhd.transactions.exception.DomainException;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    public GlobalErrorAttributes() {

        super(false);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {

        final Throwable error = getError(request);
        final Map<String, Object> errorAttributes = super.getErrorAttributes(request, includeStackTrace);
        errorAttributes.remove("message");
        if (error instanceof DomainException) {
            Throwable cause = error.getCause();
            final HttpStatus errorStatus = ((DomainException) error).getStatus();
            errorAttributes.replace("status", errorStatus.value());
            errorAttributes.replace("error", errorStatus.getReasonPhrase());
            if (cause != null) {
                Map<String, Object> causeErrorAttributes = new HashMap<>();
                causeErrorAttributes.put("exception", cause.getClass().getName());
                causeErrorAttributes.put("message", cause.getMessage());
                errorAttributes.put("cause", causeErrorAttributes);
            }
        }
        return errorAttributes;
    }

}
