package com.dhd.transactions.mapper;

import org.mapstruct.Mapper;

import com.dhd.transactions.dto.TransactionDto;
import com.dhd.transactions.entity.TransactionEntity;

@Mapper
public interface TransactionDtoMapper {

    TransactionDto transactionEntityToDto(TransactionEntity dto);

    TransactionEntity transactionDtoToEntity(TransactionDto dto);
}
