package com.dhd.transactions.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document
@Entity
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = 5369233272112856598L;

    @Id
    @JsonProperty("account_iban")
    private String accountIban;

    private BigDecimal amount;

    public AccountEntity() {

    }

    public AccountEntity(String accountIban, BigDecimal amount) {

        this.accountIban = accountIban;
        this.amount = amount;
    }

    /**
     * @return the accountIban
     */
    public String getAccountIban() {

        return accountIban;
    }

    /**
     * @param accountIban
     *            the accountIban to set
     */
    public void setAccountIban(String accountIban) {

        this.accountIban = accountIban;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {

        return amount;
    }

    /**
     * @param amount
     *            the amount to set
     */
    public void setAmount(BigDecimal amount) {

        this.amount = amount;
    }

}
