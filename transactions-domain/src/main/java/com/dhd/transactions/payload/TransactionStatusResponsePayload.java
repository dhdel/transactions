package com.dhd.transactions.payload;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.annotation.Generated;

import com.dhd.transactions.payload.enums.StatusEnum;
import com.dhd.transactions.payload.serializer.BigDecimalSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class TransactionStatusResponsePayload implements Serializable {

    private static final long serialVersionUID = -1353388395466085189L;

    private String reference;

    private StatusEnum status;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal amount;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal fee;

    @Generated("SparkTools")
    private TransactionStatusResponsePayload(Builder builder) {

        this.reference = builder.reference;
        this.status = builder.status;
        this.amount = builder.amount;
        this.fee = builder.fee;
    }

    public TransactionStatusResponsePayload() {

    }

    public TransactionStatusResponsePayload(String reference, StatusEnum status, BigDecimal amount, BigDecimal fee) {

        super();
        this.reference = reference;
        this.status = status;
        this.amount = amount;
        this.fee = fee;
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
     * @return the status
     */
    public StatusEnum getStatus() {

        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(StatusEnum status) {

        this.status = status;
    }

    /**
     * @return the amount
     */
    @JsonSerialize(using = BigDecimalSerializer.class)
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
    @JsonSerialize(using = BigDecimalSerializer.class)
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

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "TransactionStatusResponsePayload [reference=" + reference + ", status=" + status + ", amount=" + amount
                + ", fee=" + fee + "]";
    }

    /**
     * Creates builder to build {@link TransactionStatusResponsePayload}.
     * 
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {

        return new Builder();
    }

    /**
     * Builder to build {@link TransactionStatusResponsePayload}.
     */
    @Generated("SparkTools")
    public static final class Builder {

        private String reference;

        private StatusEnum status;

        private BigDecimal amount;

        private BigDecimal fee;

        private Builder() {

        }

        public Builder withReference(String reference) {

            this.reference = reference;
            return this;
        }

        public Builder withStatus(StatusEnum status) {

            this.status = status;
            return this;
        }

        public Builder withAmount(BigDecimal amount) {

            this.amount = amount;
            return this;
        }

        public Builder withFee(BigDecimal fee) {

            this.fee = fee;
            return this;
        }

        public TransactionStatusResponsePayload build() {

            return new TransactionStatusResponsePayload(this);
        }
    }

}
