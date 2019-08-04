package com.dhd.transactions.mapper;

import org.mapstruct.Mapper;

import com.dhd.transactions.dto.AccountDto;
import com.dhd.transactions.entity.AccountEntity;

@Mapper
public interface AccountMapper {

    AccountEntity accountDtoToEntity(AccountDto dto);

    AccountDto accountEntityToDto(AccountEntity entity);
}
