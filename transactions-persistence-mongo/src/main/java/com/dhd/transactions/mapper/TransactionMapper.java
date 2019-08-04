package com.dhd.transactions.mapper;

import org.mapstruct.Mapper;

import com.dhd.transactions.entity.TransactionEntity;
import com.dhd.transactions.payload.TransactionPayload;

@Mapper
public interface TransactionMapper {

    TransactionPayload transactionEntityToPayload(TransactionEntity dto);

    TransactionEntity createTransactionPayloadToEntity(TransactionPayload dto);
}
