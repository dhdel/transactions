package com.dhd.transactions.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dhd.transactions.payload.enums.ChannelEnum;
import com.dhd.transactions.payload.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransactionDto implements Serializable {

    private static final long serialVersionUID = -1353388395466085189L;

    private String reference;

    private String accountIban;

    private Date date;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fee;

    private String description;

    private ChannelEnum channel;

    public TransactionDto() {

    }

    public TransactionDto(String reference, String accountIban, Date date, BigDecimal amount, BigDecimal fee,
            String description, ChannelEnum channel) {

        super();
        this.reference = reference;
        this.accountIban = accountIban;
        this.date = date;
        this.amount = amount;
        this.fee = fee;
        this.description = description;
        this.channel = channel;
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
    public Date getDate() {

        return date;
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Date date) {

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

    /**
     * @return the channel
     */
    public ChannelEnum getChannel() {

        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(ChannelEnum channel) {

        this.channel = channel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "TransactionDto [reference=" + reference + ", accountIban=" + accountIban + ", date=" + date
                + ", amount=" + amount + ", fee=" + fee + ", description=" + description + ", channel=" + channel + "]";
    }

}
