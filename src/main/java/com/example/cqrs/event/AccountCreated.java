package com.example.cqrs.event;

public class AccountCreated extends Event {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5577767425239447381L;
	
	private String accountNumber;
	private String accountName;
	
	public AccountCreated(String accountNumber, String accountName) {
		this.accountNumber = accountNumber;
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}
	
	public String getAccountName() {
		return accountName;
	}

	@Override
	public String toString() {
		return "AccountCreated [accountNumber=" + accountNumber + ", accountName=" + accountName + "]";
	}
	
}