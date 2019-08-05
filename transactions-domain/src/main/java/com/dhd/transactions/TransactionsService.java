package com.dhd.transactions;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.dhd.transactions.dto.AccountDto;
import com.dhd.transactions.dto.SearchTransactionDto;
import com.dhd.transactions.dto.TransactionDto;
import com.dhd.transactions.exception.DatabaseOperationException;
import com.dhd.transactions.exception.IllegalOperationException;
import com.dhd.transactions.exception.NotFoundException;
import com.dhd.transactions.payload.TransactionPayload;
import com.dhd.transactions.payload.TransactionStatusRequestPayload;
import com.dhd.transactions.payload.TransactionStatusResponsePayload;
import com.dhd.transactions.payload.enums.ChannelEnum;
import com.dhd.transactions.payload.enums.StatusEnum;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionsService {

    Mono<String> insertTransaction(TransactionPayload payload);

    Flux<TransactionPayload> searchTransactions(SearchTransactionDto search);

    Mono<AccountDto> getAccount(String iban);

    Mono<String> updateAccount(TransactionPayload payload);

    Mono<TransactionDto> getTransactionByReference(String reference);

    default Mono<String> processTransaction(TransactionPayload payload) {

        final Mono<AccountDto> account = getAccount(payload.getAccountIban());
        return account
                .flatMap(
                        acc -> acc.getAmount().compareTo(payload.getAmount().negate()) >= 0 //
                                ? updateAccount(payload).flatMap(p -> insertTransaction(payload))
                                : Mono.error(
                                        new IllegalOperationException(
                                                "A transaction that leaves the total account balance bellow 0 is not allowed! - "
                                                        + "[payload: " + payload + "];[account: " + acc + "]")))
                .onErrorResume(
                        error -> (error instanceof IllegalOperationException) //
                                ? Mono.error(error)
                                : Mono.error(new DatabaseOperationException("Operation failed!")))
                .switchIfEmpty(Mono.error(new NotFoundException("Account not found!")));

    }

    default Mono<TransactionStatusResponsePayload> transactionStatus(TransactionStatusRequestPayload query) {

        return getTransactionByReference(query.getReference())
                .filter(
                        t -> StringUtils.isEmpty(query.getChannel())
                                || t.getChannel().equals(ChannelEnum.valueOf(query.getChannel().toUpperCase())))
                .map(this::getPayload)
                .switchIfEmpty(
                        Mono.just(
                                TransactionStatusResponsePayload
                                        .builder()
                                        .withReference(query.getReference())
                                        .withStatus(StatusEnum.INVALID)
                                        .build()));

    }

    default LocalDate convertToLocalDate(Date dateToConvert) {

        return dateToConvert.toInstant().atZone(ZoneId.of("UTC")).toLocalDate();
    }

    default TransactionStatusResponsePayload getPayload(TransactionDto dto) {

        LocalDate date = LocalDate.now();

        int dateCompare = convertToLocalDate(dto.getDate()).compareTo(date);
        TransactionStatusResponsePayload p = new TransactionStatusResponsePayload();
        switch (dto.getChannel()) {
            case ATM:
                p = TransactionStatusResponsePayload
                        .builder()
                        .withReference(dto.getReference())
                        .withAmount(dto.getAmount().subtract(dto.getFee()))
                        .build();
                if (dateCompare > 0)
                    p.setStatus(StatusEnum.PENDING);
                else if (dateCompare < 0)
                    p.setStatus(StatusEnum.SETTLED);
                else
                    p.setStatus(StatusEnum.PENDING);
                break;
            case CLIENT:
                p = TransactionStatusResponsePayload
                        .builder()
                        .withReference(dto.getReference())
                        .withAmount(dto.getAmount().subtract(dto.getFee()))
                        .build();
                if (dateCompare > 0)
                    p.setStatus(StatusEnum.FUTURE);
                else if (dateCompare < 0)
                    p.setStatus(StatusEnum.SETTLED);
                else
                    p.setStatus(StatusEnum.PENDING);
                break;
            case INTERNAL:
                p = TransactionStatusResponsePayload
                        .builder()
                        .withReference(dto.getReference())
                        .withAmount(dto.getAmount())
                        .withFee(dto.getFee())
                        .build();
                if (dateCompare > 0)
                    p.setStatus(StatusEnum.FUTURE);
                else if (dateCompare < 0)
                    p.setStatus(StatusEnum.SETTLED);
                else
                    p.setStatus(StatusEnum.PENDING);
        }
        return p;

    }

}
