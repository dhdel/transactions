package com.dhd.webflux.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhd.transactions.payload.TransactionPayload;

public class CreateTransactionPayloadValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return TransactionPayload.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountIban", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount", "field.required");
        TransactionPayload request = (TransactionPayload) target;
        // System.out.println(request.getDate());
        // if (request.getCode() != null && request.getCode().trim().length() < 6) {
        // errors.rejectValue(
        // "code",
        // "field.min.length",
        // new Object[] { Integer.valueOf(6) },
        // "The code must be at least [6] characters in length.");
        // }
    }
}
