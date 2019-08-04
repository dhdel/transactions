package com.dhd.transactions.payload;

import java.io.Serializable;

public class TransactionStatusRequestPayload implements Serializable {

    private static final long serialVersionUID = 9080056168359770586L;

    private String reference;

    private String channel;

    public TransactionStatusRequestPayload() {

    }

    public TransactionStatusRequestPayload(String reference, String channel) {

        super();
        this.reference = reference;
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
     * @return the channel
     */
    public String getChannel() {

        return channel;
    }

    /**
     * @param channel
     *            the channel to set
     */
    public void setChannel(String channel) {

        this.channel = channel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "TransactionStatusRequestPayload [reference=" + reference + ", channel=" + channel + "]";
    }

}
