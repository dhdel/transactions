package com.dhd.transactions.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.dhd.transactions.entity.TransactionEntity;

import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<TransactionEntity, Serializable> {

    Flux<TransactionEntity> findAllByAccountIbanOrderByDateDesc(String accountIban);

    Flux<TransactionEntity> findAllByAccountIbanOrderByAmountAsc(String accountIban);

    Flux<TransactionEntity> findAllByAccountIbanOrderByAmountDesc(String accountIban);
}
