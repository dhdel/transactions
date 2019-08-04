package com.dhd.webflux;

import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.dhd.transactions.TransactionsService;
import com.dhd.transactions.payload.TransactionPayload;
import com.dhd.transactions.payload.TransactionStatusRequestPayload;
import com.dhd.webflux.error.GlobalErrorAttributes;

import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest({ TransactionsHandler.class, TransactionsRouter.class, GlobalErrorAttributes.class })
public class TransactionsHandlerValidationTests {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionsService service;

    @Test
    public void createTransactionWithoutIbanShouldReturnBadRequest() throws Exception {

        TransactionPayload payload = new TransactionPayload(
                "reference",
                null,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(1000.01),
                new BigDecimal(1.02),
                "description");

        when(service.processTransaction(any(TransactionPayload.class))).thenReturn(Mono.just("ES999999999"));

        webTestClient
                .post()
                .uri("/transactions") //
                .body(Mono.just(payload), TransactionPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void createTransactionWithoutAmountShouldReturnBadRequest() throws Exception {

        TransactionPayload payload = new TransactionPayload(
                "reference",
                "iban",
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                null,
                new BigDecimal(1.02),
                "description");

        when(service.processTransaction(any(TransactionPayload.class))).thenReturn(Mono.just("ES999999999"));

        webTestClient
                .post()
                .uri("/transactions") //
                .body(Mono.just(payload), TransactionPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void searchTransactionsWithIncorrectSortShouldReturnBadRequest() throws Exception {

        webTestClient
                .get() //
                .uri(uriBuilder -> uriBuilder.path("/transactions/ES999999999").queryParam("sort", "xxxx").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();

    }

    @Test
    public void transactionStatusWithoutReferenceShouldReturnBadRequest() throws Exception {

        TransactionStatusRequestPayload payload = new TransactionStatusRequestPayload(null, "channel");

        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(payload), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    public void transactionStatusWithIncorrectChannelShouldReturnBadRequest() throws Exception {

        TransactionStatusRequestPayload payload = new TransactionStatusRequestPayload("reference", "channel");

        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(payload), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
