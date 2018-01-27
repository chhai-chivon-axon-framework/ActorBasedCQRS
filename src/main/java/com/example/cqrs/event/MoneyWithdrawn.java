package com.example.cqrs.event;

import java.math.BigDecimal;

public class MoneyWithdrawn extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7876662770710605707L;
	
	private BigDecimal amount;
	private String accountNumber;
	
	public MoneyWithdrawn(BigDecimal amount, String accountNumber) {
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
		return "MoneyWithdrawn [amount=" + amount + ", accountNumber=" + accountNumber + "]";
	}
	
}
