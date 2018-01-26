package com.example.cqrs.restcontroller;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.cqrs.event.AccountCreated;
import com.example.cqrs.event.MoneyDeposited;
import com.example.cqrs.event.MoneyWithdrawn;
import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.readmodel.AccountEntryRepository;

@Component
public class BankAccountReplayEventListener implements ReplayEventListener<AccountEntry, Object> {
	
	@Autowired
	private AccountEntryRepository repository;
	
	@Override
	public AccountEntry applyEvent(Object event) {
		if (event instanceof AccountCreated) {
			AccountCreated newAccount = ((AccountCreated) event);
			AccountEntry accountEntry = new AccountEntry();
			accountEntry.setAccountName(newAccount.getAccountName());
			accountEntry.setAccountNumber(newAccount.getAccountNumber());
			accountEntry.setBalance(BigDecimal.ZERO);
			accountEntry.setId(newAccount.getAccountId());
			return repository.save(accountEntry);
		}
		if (event instanceof MoneyDeposited) {
			MoneyDeposited depositedAccount = (MoneyDeposited) event;
			AccountEntry entry = repository.findOne(UUID.fromString("81379c8f-edde-4536-96c8-cd634b37343b"));
			if (entry != null) {
				entry.setBalance(entry.getBalance().add(depositedAccount.getAmount()));
				return repository.save(entry);
			}
		}
		if (event instanceof MoneyWithdrawn) {
			MoneyWithdrawn withdrawnAccount = (MoneyWithdrawn) event;
			AccountEntry entry = repository.findOne(UUID.fromString("81379c8f-edde-4536-96c8-cd634b37343b"));
			if (entry != null) {
				entry.setBalance(entry.getBalance().subtract(withdrawnAccount.getAmount()));
				return repository.save(entry);
			}
		}
		return null;
	}
	
}