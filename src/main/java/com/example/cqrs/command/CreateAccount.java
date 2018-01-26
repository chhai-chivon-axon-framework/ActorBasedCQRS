package com.example.cqrs.command;

import java.util.UUID;

public class CreateAccount extends Command {	
	/**
	 * 
	 */
	private static final long serialVersionUID = 25890934444776989L;
	
	private UUID accountId;
	private String accountNumber;
	private String accountName;
	
	public CreateAccount(String accountNumber, String accountName) {
		this.accountId = UUID.randomUUID();
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
		return "CreateAccount [accountId=" + accountId + ", accountNumber=" + accountNumber + ", accountName=" + accountName + "]";
	}	
	
}
