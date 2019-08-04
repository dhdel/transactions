package com.dhd.transactions.dto;

import java.io.Serializable;

import com.dhd.transactions.payload.enums.SortEnum;

public class SearchTransactionDto implements Serializable {

    private static final long serialVersionUID = 9080056168359770586L;

    private String accountIban;

    private SortEnum sort;

    public SearchTransactionDto() {

    }

    public SearchTransactionDto(String accountIban, SortEnum sort) {

        super();
        this.accountIban = accountIban;
        this.sort = sort;
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
     * @return the sort
     */
    public SortEnum getSort() {

        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(SortEnum sort) {

        this.sort = sort;
    }

}
