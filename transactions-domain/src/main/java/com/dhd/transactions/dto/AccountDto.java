package com.dhd.transactions.dto;

import java.math.BigDecimal;

public class AccountDto {

    private String accountIban;

    private BigDecimal amount;

    public AccountDto(String accountIban, BigDecimal amount) {

        this.accountIban = accountIban;
        this.amount = amount;
    }

    public AccountDto() {

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
