package com.dhd.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dhd.transactions.dto.AccountDto;
import com.dhd.transactions.dto.SearchTransactionDto;
import com.dhd.transactions.dto.TransactionDto;
import com.dhd.transactions.entity.AccountEntity;
import com.dhd.transactions.entity.TransactionEntity;
import com.dhd.transactions.mapper.AccountMapper;
import com.dhd.transactions.mapper.TransactionDtoMapper;
import com.dhd.transactions.mapper.TransactionMapper;
import com.dhd.transactions.payload.TransactionPayload;
import com.dhd.transactions.repository.AccountRepository;
import com.dhd.transactions.repository.TransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionsServiceImpl implements TransactionsService {

	private final TransactionRepository transactionsRepository;

	private final TransactionMapper transactionsMapper;

	private final AccountRepository accountRepository;

	private final AccountMapper accountMapper;

	private final TransactionDtoMapper transactionDtoMapper;

	@Autowired
	public TransactionsServiceImpl(TransactionRepository trepository, TransactionMapper tmapper,
			AccountRepository arepository, AccountMapper amapper, TransactionDtoMapper transactionDtoMapper) {

		this.transactionsRepository = trepository;
		this.transactionsMapper = tmapper;
		this.accountRepository = arepository;
		this.accountMapper = amapper;
		this.transactionDtoMapper = transactionDtoMapper;
	}

	@Override
	public Mono<String> insertTransaction(TransactionPayload payload) {

		return transactionsRepository.save(transactionsMapper.createTransactionPayloadToEntity(payload)) //
				.map(TransactionEntity::getReference);
	}

	@Override
	public Flux<TransactionPayload> searchTransactions(SearchTransactionDto search) {

		switch (search.getSort()) {
		case NONE:
			return transactionsRepository.findAllByAccountIbanOrderByDateDesc(search.getAccountIban())
					.map(transactionsMapper::transactionEntityToPayload);
		case ASC:
			return transactionsRepository.findAllByAccountIbanOrderByAmountAsc(search.getAccountIban())
					.map(transactionsMapper::transactionEntityToPayload);
		case DESC:
			return transactionsRepository.findAllByAccountIbanOrderByAmountDesc(search.getAccountIban())
					.map(transactionsMapper::transactionEntityToPayload);
		}
		return Flux.empty();
	}

	@Override
	public Mono<AccountDto> getAccount(String iban) {

		return accountRepository.findById(iban).map(accountMapper::accountEntityToDto);
	}

	@Override
	public Mono<String> updateAccount(TransactionPayload payload) {

		return accountRepository.findById(payload.getAccountIban()) //
				.map(a -> getUpdatedAccount(a, payload)).flatMap(accountRepository::save)
				.map(AccountEntity::getAccountIban);

	}

	private AccountEntity getUpdatedAccount(AccountEntity account, TransactionPayload payload) {

		account.setAmount(account.getAmount().add(payload.getAmount()));
		return account;
	}

	@Override
	public Mono<TransactionDto> getTransactionByReference(String reference) {

		Mono<TransactionEntity> mono = transactionsRepository.findById(reference);

		return mono.map(transactionDtoMapper::transactionEntityToDto);
	}

}
