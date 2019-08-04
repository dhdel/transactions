package com.dhd.transactions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Before;
import org.junit.Test;

import com.dhd.transactions.dto.AccountDto;
import com.dhd.transactions.exception.DatabaseOperationException;
import com.dhd.transactions.exception.IllegalOperationException;
import com.dhd.transactions.exception.NotFoundException;
import com.dhd.transactions.payload.TransactionPayload;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransactionsServiceTests {

    TransactionsService service;

    @Before
    public void setUp() {

        service = spy(TransactionsService.class);
    }

    @Test
    public void processTransactionHappyPath() {

        final String iban = "ES1234567890";

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(100.01),
                new BigDecimal(1.02),
                "description");

        Mono<AccountDto> account = Mono.just(new AccountDto("ES1234567890", new BigDecimal(1000)));

        doReturn(account).when(service).getAccount(anyString());
        doReturn(Mono.just(iban)).when(service).updateAccount(any(TransactionPayload.class));
        doReturn(Mono.just(iban)).when(service).insertTransaction(any(TransactionPayload.class));

        Mono<String> result = service.processTransaction(payload);
        StepVerifier.create(result).expectNext(iban).verifyComplete();
    }

    @Test
    public void processTransactionWhenAccountDoesntExistThrowException() {

        final String iban = "ES1234567890";

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(100.01),
                new BigDecimal(1.02),
                "description");

        Mono<AccountDto> account = Mono.empty();

        doReturn(account).when(service).getAccount(anyString());
        doReturn(Mono.just(iban)).when(service).updateAccount(any(TransactionPayload.class));
        doReturn(Mono.just(iban)).when(service).insertTransaction(any(TransactionPayload.class));

        Mono<String> result = service.processTransaction(payload);
        StepVerifier
                .create(result) //
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    public void processTransactionWhenUpdateAccountFailsThrowException() {

        final String iban = "ES1234567890";

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(100.01),
                new BigDecimal(1.02),
                "description");

        Mono<AccountDto> account = Mono.just(new AccountDto("ES1234567890", new BigDecimal(1000)));

        doReturn(account).when(service).getAccount(anyString());
        doThrow(new RuntimeException("Custom exception!")).when(service).updateAccount(any(TransactionPayload.class));
        doReturn(Mono.just(iban)).when(service).insertTransaction(any(TransactionPayload.class));

        Mono<String> result = service.processTransaction(payload);
        StepVerifier
                .create(result) //
                .expectError(DatabaseOperationException.class)
                .verify();
    }

    @Test
    public void processTransactionWhenInsertTransactionFailsThrowException() {

        final String iban = "ES1234567890";

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(100.01),
                new BigDecimal(1.02),
                "description");

        Mono<AccountDto> account = Mono.just(new AccountDto("ES1234567890", new BigDecimal(1000)));

        doReturn(account).when(service).getAccount(anyString());
        doReturn(Mono.just(iban)).when(service).updateAccount(any(TransactionPayload.class));
        doThrow(new RuntimeException("Custom exception")).when(service).insertTransaction(
                any(TransactionPayload.class));

        Mono<String> result = service.processTransaction(payload);
        StepVerifier
                .create(result) //
                .expectError(DatabaseOperationException.class)
                .verify();
    }

    @Test
    public void processTransactionWhenInsufficienAmmountThrowException() {

        final String iban = "ES1234567890";

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-10000.01),
                new BigDecimal(1.02),
                "description");

        Mono<AccountDto> account = Mono.just(new AccountDto("ES1234567890", new BigDecimal(1000)));

        doReturn(account).when(service).getAccount(anyString());
        doReturn(Mono.just(iban)).when(service).updateAccount(any(TransactionPayload.class));
        doReturn(Mono.just(iban)).when(service).insertTransaction(any(TransactionPayload.class));

        Mono<String> result = service.processTransaction(payload);
        StepVerifier
                .create(result) //
                .expectError(IllegalOperationException.class)
                .verify();
    }
}
