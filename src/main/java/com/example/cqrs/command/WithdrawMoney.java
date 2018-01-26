package com.example.cqrs.command;

import java.math.BigDecimal;

public class WithdrawMoney extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7163119516202871790L;
	
	private BigDecimal amount;
	
	public WithdrawMoney(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "WithdrawMoney [amount=" + amount + "]";
	}
	
}
