package com.example.cqrs.command;

import java.math.BigDecimal;

public class DepositMoney extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5404086989680533946L;
	
	private BigDecimal amount;
	private String accountNumber;
	
	public DepositMoney(BigDecimal amount, String accountNumber) {
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
		return "DepositMoney [amount=" + amount + ", accountNumber=" + accountNumber + "]";
	}	
	
}