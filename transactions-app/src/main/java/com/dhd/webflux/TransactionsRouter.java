package com.dhd.webflux;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransactionsRouter {

    @Bean
    public RouterFunction<ServerResponse> route(TransactionsHandler handler) {

        return RouterFunctions
                .route(
                        GET("/transactions/{account_iban}").and(accept(MediaType.APPLICATION_JSON)),
                        handler::searchTransactions)
                .andRoute(
                        POST("/transactions/status").and(accept(MediaType.APPLICATION_JSON)),
                        handler::transactionStatus)
                .andRoute(POST("/transactions").and(accept(MediaType.APPLICATION_JSON)), handler::insertTransaction);
    }

}
