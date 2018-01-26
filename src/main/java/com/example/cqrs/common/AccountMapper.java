package com.example.cqrs.common;

import com.example.cqrs.actor.Account;
import com.example.cqrs.readmodel.AccountEntry;

public class AccountMapper implements Mapper <Account, AccountEntry> {

	@Override
	public AccountEntry map(Account account) {
		AccountEntry entry = new AccountEntry();
		entry.setAccountName(account.getAccountName());
		entry.setAccountNumber(account.getAccountNumber());
		entry.setBalance(account.getBalance());
		return entry;
	}

}
