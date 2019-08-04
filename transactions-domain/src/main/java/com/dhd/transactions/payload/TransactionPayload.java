package com.dhd.transactions.payload;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.dhd.transactions.payload.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransactionPayload implements Serializable {

    private static final long serialVersionUID = -1353388395466085189L;

    private String reference;

    private String accountIban;

    private ZonedDateTime date;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fee;

    private String description;

    public TransactionPayload() {

    }

    public TransactionPayload(String reference, String accountIban, ZonedDateTime date, BigDecimal amount,
            BigDecimal fee, String description) {

        super();
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
    }

    /**
     * @return the reference
     */
    public String getReference() {

        return reference;
    }

    /**
     * @param reference
     *            the reference to set
     */
    public void setReference(String reference) {

        this.reference = reference;
    }

    /**
     * @return the date
     */
    public ZonedDateTime getDate() {

        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(ZonedDateTime date) {

        this.date = date;
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

    /**
     * @return the fee
     */
    public BigDecimal getFee() {

        return fee;
    }

    /**
     * @param fee
     *            the fee to set
     */
    public void setFee(BigDecimal fee) {

        this.fee = fee;
    }

    /**
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {

        this.description = description;
    }

    /**
     * @return the accountIban
     */
    @JsonProperty("account_iban")
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "CreateTransactionPayload [reference=" + reference + ", accountIban=" + accountIban + ", date=" + date
                + ", amount=" + amount + ", fee=" + fee + ", description=" + description + "]";
    }

}
