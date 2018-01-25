package com.example.cqrs.command;

import java.math.BigDecimal;

public class WithdrawMoney extends Command {
	
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
