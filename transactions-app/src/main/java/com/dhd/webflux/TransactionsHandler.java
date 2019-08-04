package com.dhd.webflux;

import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.dhd.transactions.TransactionsService;
import com.dhd.transactions.dto.SearchTransactionDto;
import com.dhd.transactions.exception.ParameterValidationException;
import com.dhd.transactions.exception.PayloadValidationException;
import com.dhd.transactions.payload.TransactionPayload;
import com.dhd.transactions.payload.TransactionStatusRequestPayload;
import com.dhd.transactions.payload.enums.SortEnum;
import com.dhd.webflux.validator.CreateTransactionPayloadValidator;
import com.dhd.webflux.validator.TransactionStatusRequestPayloadValidator;

import reactor.core.publisher.Mono;

@Component
public class TransactionsHandler {

    private final TransactionsService service;

    private final Validator createTransactionPayloadValidator = new CreateTransactionPayloadValidator();

    private final Validator transactionStatusRequestPayloadValidator = new TransactionStatusRequestPayloadValidator();

    @Autowired
    public TransactionsHandler(TransactionsService transactionsService) {

        this.service = transactionsService;
    }

    public Mono<ServerResponse> insertTransaction(ServerRequest request) {

        return request //
                .bodyToMono(TransactionPayload.class)
                .flatMap(this::validateCreateTransactionPayload)
                .flatMap(service::processTransaction)
                .flatMap(
                        ref -> ServerResponse
                                .created(URI.create("/transactions/" + ref)) //
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromObject(ref)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    public Mono<ServerResponse> searchTransactions(ServerRequest request) {

        return Mono
                .just(service.searchTransactions(validateSearchTransactionParameters(request))) //
                .flatMap(
                        flux -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                                flux,
                                TransactionPayload.class));
    }

    public Mono<ServerResponse> transactionStatus(ServerRequest request) {

        return request //
                .bodyToMono(TransactionStatusRequestPayload.class)
                .flatMap(this::validateTransactionStatusRequestPayload)
                .flatMap(service::transactionStatus)
                .flatMap(
                        ref -> ServerResponse
                                .ok() //
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromObject(ref)))
                .switchIfEmpty(ServerResponse.badRequest().build());
    }

    private Mono<TransactionPayload> validateCreateTransactionPayload(TransactionPayload createTransaction) {

        Errors errors = new BeanPropertyBindingResult(createTransaction, "createTransaction");
        createTransactionPayloadValidator.validate(createTransaction, errors);
        if (errors.hasErrors()) {
            return Mono.error(new PayloadValidationException(errors.toString()));
        }
        return Mono.just(createTransaction);
    }

    private Mono<TransactionStatusRequestPayload> validateTransactionStatusRequestPayload(
            TransactionStatusRequestPayload statusPayload) {

        Errors errors = new BeanPropertyBindingResult(statusPayload, "statusPayload");
        transactionStatusRequestPayloadValidator.validate(statusPayload, errors);
        if (errors.hasErrors()) {
            throw new PayloadValidationException(errors.toString());
        }
        return Mono.just(statusPayload);
    }

    private SearchTransactionDto validateSearchTransactionParameters(ServerRequest request) {

        String iban = request.pathVariable("account_iban");
        if (StringUtils.isEmpty(iban))
            throw new ParameterValidationException("Account iban not found");
        SortEnum sort = SortEnum.NONE;
        if (request.queryParam("sort").isPresent())
            try {
                sort = SortEnum.valueOf(request.queryParam("sort").get().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ParameterValidationException("Invalid sorting type");
            }
        return new SearchTransactionDto(iban, sort);
    }

}
