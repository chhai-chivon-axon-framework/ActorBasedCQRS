package com.example.cqrs.command;

import java.math.BigDecimal;

public class WithdrawMoney extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7163119516202871790L;
	
	private BigDecimal amount;
	private String accountNumber;
	
	public WithdrawMoney(BigDecimal amount, String accountNumber) {
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
		return "WithdrawMoney [amount=" + amount + ", accountNumber=" + accountNumber + "]";
	}
	
}
