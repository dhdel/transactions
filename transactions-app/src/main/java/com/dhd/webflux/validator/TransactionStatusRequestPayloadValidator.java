package com.dhd.webflux.validator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.dhd.transactions.payload.TransactionStatusRequestPayload;
import com.dhd.transactions.payload.enums.ChannelEnum;

public class TransactionStatusRequestPayloadValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {

        return TransactionStatusRequestPayload.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reference", "field.required");
        TransactionStatusRequestPayload request = (TransactionStatusRequestPayload) target;
        if (StringUtils.isNotEmpty(request.getChannel()))
            try {
                ChannelEnum.valueOf(request.getChannel().toUpperCase());
            } catch (IllegalArgumentException e) {
                errors.rejectValue(
                        "channel",
                        "field.value",
                        new Object[] { ChannelEnum.ATM, ChannelEnum.CLIENT, ChannelEnum.INTERNAL },
                        "The chunnel must be one of " + ChannelEnum.ATM + "," + ChannelEnum.CLIENT + ","
                                + ChannelEnum.INTERNAL);
            }
    }
}
