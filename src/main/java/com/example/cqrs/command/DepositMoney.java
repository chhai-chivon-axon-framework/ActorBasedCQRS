package com.example.cqrs.command;

import java.math.BigDecimal;

public class DepositMoney extends Command {
	
	private BigDecimal amount;
	
	public DepositMoney(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "DepositMoney [amount=" + amount + "]";
	}	
	
}