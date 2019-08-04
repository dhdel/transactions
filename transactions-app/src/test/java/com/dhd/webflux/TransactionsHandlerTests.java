package com.dhd.webflux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.dhd.transactions.entity.AccountEntity;
import com.dhd.transactions.entity.TransactionEntity;
import com.dhd.transactions.mapper.TransactionDtoMapper;
import com.dhd.transactions.mapper.TransactionMapper;
import com.dhd.transactions.payload.TransactionPayload;
import com.dhd.transactions.payload.TransactionStatusRequestPayload;
import com.dhd.transactions.payload.TransactionStatusResponsePayload;
import com.dhd.transactions.payload.enums.ChannelEnum;
import com.dhd.transactions.payload.enums.StatusEnum;
import com.dhd.transactions.repository.AccountRepository;
import com.dhd.transactions.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsHandlerTests {

    private final String ACCOUNT_IBAN = "ES9820385778983000760236";

    private final BigDecimal finalAmount = new BigDecimal(190.20);

    private final BigDecimal amount = new BigDecimal(193.38);

    private final BigDecimal fee = new BigDecimal(3.18);

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TransactionRepository trepository;

    @MockBean
    private AccountRepository arepository;

    @Autowired
    private TransactionMapper tmapper;

    @Autowired
    private TransactionDtoMapper tdtomapper;

    @Before
    public void setUp() {

        AccountEntity account = new AccountEntity(ACCOUNT_IBAN, new BigDecimal(1000.00));
        when(arepository.findById(anyString())).thenReturn(Mono.just(account));
    }

    @Test
    public void createTransactionHappyPath() throws Exception {

        TransactionPayload payload = new TransactionPayload(
                "12345A",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(1000.01),
                new BigDecimal(1.02),
                "description");

        AccountEntity account = new AccountEntity(ACCOUNT_IBAN, new BigDecimal(1000.00));
        when(arepository.save(any(AccountEntity.class))).thenReturn(Mono.just(account));

        TransactionEntity transaction = tmapper.createTransactionPayloadToEntity(payload);
        when(trepository.save(any(TransactionEntity.class))).thenReturn(Mono.just(transaction));

        webTestClient
                .post()
                .uri("/transactions") //
                .body(Mono.just(payload), TransactionPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void createTransactionWithAmountSuperiorToAvailabilityShouldReturnBadRequest() throws Exception {

        TransactionPayload payload = new TransactionPayload(
                "12345A",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-1000.01),
                new BigDecimal(1.02),
                "description");

        AccountEntity account = new AccountEntity(ACCOUNT_IBAN, new BigDecimal(1000.00));
        when(arepository.save(any(AccountEntity.class))).thenReturn(Mono.just(account));

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
    public void searchTransactionsHappyPath() throws Exception {

        TransactionPayload payload1 = new TransactionPayload(
                "12345B",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-100.01),
                new BigDecimal(1.02),
                "description");

        TransactionPayload payload2 = new TransactionPayload(
                "12345B",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-100.01),
                new BigDecimal(1.02),
                "description");

        Flux<TransactionEntity> transactions = Flux.fromArray(
                new TransactionEntity[] { tmapper.createTransactionPayloadToEntity(payload1),
                        tmapper.createTransactionPayloadToEntity(payload2) });
        when(trepository.findAllByAccountIbanOrderByAmountDesc(anyString())).thenReturn(transactions);

        webTestClient
                .get() //
                .uri(uriBuilder -> uriBuilder.path("/transactions/" + ACCOUNT_IBAN).queryParam("sort", "DESC").build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TransactionPayload.class)
                .hasSize(2);

    }

    @Test
    public void searchTransactionsWithoutSortShouldReturnOK() throws Exception {

        TransactionPayload payload1 = new TransactionPayload(
                "12345B",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-100.01),
                new BigDecimal(1.02),
                "description");

        TransactionPayload payload2 = new TransactionPayload(
                "12345B",
                ACCOUNT_IBAN,
                Instant.now().atZone(ZoneId.of("Europe/Paris")),
                new BigDecimal(-100.01),
                new BigDecimal(1.02),
                "description");

        Flux<TransactionEntity> transactions = Flux.fromArray(
                new TransactionEntity[] { tmapper.createTransactionPayloadToEntity(payload1),
                        tmapper.createTransactionPayloadToEntity(payload2) });
        when(trepository.findAllByAccountIbanOrderByDateDesc(anyString())).thenReturn(transactions);

        webTestClient
                .get() //
                .uri(uriBuilder -> uriBuilder.path("/transactions/" + ACCOUNT_IBAN).build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(TransactionPayload.class)
                .hasSize(2);

    }

    // A
    @Test
    public void transactionStatusInvalid() {

        final String reference = "12345Z";
        // Given: A transaction that is not stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "CLIENT");
        // When: I check the status from any channel
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.INVALID);
        when(trepository.findById(anyString())).thenReturn(Mono.empty());

        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'INVALID'
                .equals(expected);
    }

    // B
    @Test
    public void transactionClientOrAtmChannelStatusSettled() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "CLIENT");
        // When: I check the status from CLIENT or ATM channel
        // And the transaction date is before today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.SETTLED);
        expected.setAmount(finalAmount);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
                amount,
                fee,
                "descr",
                ChannelEnum.CLIENT);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'SETTLED'
                // And the amount substracting the fee
                .equals(expected);
    }

    // C
    @Test
    public void transactionInternalChannelStatusSettled() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "INTERNAL");
        // When: I check the status from INTERNAL channel
        // And the transaction date is before today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.SETTLED);
        expected.setAmount(amount);
        expected.setFee(fee);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                Date.from(Instant.now().minus(2, ChronoUnit.DAYS)),
                amount,
                fee,
                "descr",
                ChannelEnum.INTERNAL);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'SETTLED'
                // And the amount
                // And the fee
                .equals(expected);
    }

    // D
    @Test
    public void transactionClientOrAtmChannelStatusPending() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "ATM");
        // When: I check the status from CLIENT or ATM channel
        // And the transaction date is equals to today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.PENDING);
        expected.setAmount(finalAmount);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                new Date(),
                amount,
                fee,
                "descr",
                ChannelEnum.ATM);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'PENDING'
                // And the amount substracting the fee
                .equals(expected);
    }

    // E
    @Test
    public void transactionInternalChannelStatusPending() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "INTERNAL");
        // When: I check the status from INTERNAL channel
        // And the transaction date is equals to today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.PENDING);
        expected.setAmount(amount);
        expected.setFee(fee);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                new Date(),
                amount,
                fee,
                "descr",
                ChannelEnum.INTERNAL);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'PENDING'
                // And the amount
                // And the fee
                .equals(expected);
    }

    // F
    @Test
    public void transactionClientChannelStatusFuture() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "CLIENT");
        // When: I check the status from CLIENT or ATM channel
        // And the transaction date is greater than today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.FUTURE);
        expected.setAmount(amount);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                Date.from(Instant.now().plus(2, ChronoUnit.DAYS)),
                amount,
                fee,
                "descr",
                ChannelEnum.CLIENT);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'FUTURE'
                // And the amount substracting the fee
                .equals(expected);
    }

    // G
    @Test
    public void transactionAtmChannelStatusPending() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "ATM");
        // When: I check the status from CLIENT or ATM channel
        // And the transaction date is greater than today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.PENDING);
        expected.setAmount(finalAmount);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                Date.from(Instant.now().plus(2, ChronoUnit.DAYS)),
                amount,
                fee,
                "descr",
                ChannelEnum.ATM);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'PENDING'
                // And the amount substracting the fee
                .equals(expected);
    }

    // H
    @Test
    public void transactionInternalChannelStatusFuture() {

        final String reference = "12345A";
        // Given: A transaction that is stored in our system
        TransactionStatusRequestPayload request = new TransactionStatusRequestPayload(reference, "INTERNAL");
        // When: I check the status from INTERNAL channel
        // And the transaction date is greater than today
        TransactionStatusResponsePayload expected = new TransactionStatusResponsePayload();
        expected.setReference(reference);
        expected.setStatus(StatusEnum.FUTURE);
        expected.setAmount(amount);
        expected.setFee(fee);
        TransactionEntity entity = new TransactionEntity(
                reference,
                ACCOUNT_IBAN,
                Date.from(Instant.now().plus(2, ChronoUnit.DAYS)),
                amount,
                fee,
                "descr",
                ChannelEnum.INTERNAL);
        when(trepository.findById(anyString())).thenReturn(Mono.just(entity));
        webTestClient
                .post()
                .uri("/transactions/status") //
                .body(Mono.just(request), TransactionStatusRequestPayload.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(TransactionStatusResponsePayload.class)
                // Then: The system returns the status 'FUTURE'
                // And the amount
                // And the fee
                .equals(expected);
    }

}
