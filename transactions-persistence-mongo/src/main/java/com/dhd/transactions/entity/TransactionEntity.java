package com.dhd.transactions.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import com.dhd.transactions.payload.enums.ChannelEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document
@Entity
public class TransactionEntity implements Serializable {

    private static final long serialVersionUID = -560068985554529816L;

    @Id
    private String reference;

    @JsonProperty("account_iban")
    private String accountIban;

    private Date date;

    private BigDecimal amount;

    private BigDecimal fee;

    private String description;

    private ChannelEnum channel = ChannelEnum.CLIENT;

    public TransactionEntity() {

    }

    public TransactionEntity(String reference, String accountIban, Date date, BigDecimal amount, BigDecimal fee,
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

        if (StringUtils.isEmpty(reference))
            this.reference = UUID.randomUUID().toString();
        else
            this.reference = reference;
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

}
