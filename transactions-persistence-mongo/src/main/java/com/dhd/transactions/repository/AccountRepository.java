package com.dhd.transactions.repository;

import java.io.Serializable;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.dhd.transactions.entity.AccountEntity;

@Repository
public interface AccountRepository extends ReactiveMongoRepository<AccountEntity, Serializable> {

}
