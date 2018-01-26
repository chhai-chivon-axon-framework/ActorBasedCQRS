package com.example.cqrs.event;

import java.util.UUID;

public class AccountCreated extends Event {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5577767425239447381L;
	
	private UUID accountId;
	private String accountNumber;
	private String accountName;
	
	public AccountCreated(UUID accountId, String accountNumber, String accountName) {
		this.accountId = accountId;
		this.accountNumber = accountNumber;
		this.accountName = accountName;
	}
	
	public UUID getAccountId() {
		return accountId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public String getAccountName() {
		return accountName;
	}

	@Override
	public String toString() {
		return "AccountCreated [accountId=" + accountId + ", accountNumber=" + accountNumber + ", accountName="
				+ accountName + "]";
	}
	
}