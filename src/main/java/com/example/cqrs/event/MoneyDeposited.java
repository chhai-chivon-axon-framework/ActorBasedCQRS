package com.example.cqrs.event;

import java.math.BigDecimal;

public class MoneyDeposited extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794109229414985744L;
	
	private BigDecimal amount;
	private String accountNumber;
	
	public MoneyDeposited(BigDecimal amount, String accountNumber) {
		this.amount = amount;
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public String toString() {
		return "MoneyDeposited [amount=" + amount + ", accountNumber=" + accountNumber + "]";
	}

}
