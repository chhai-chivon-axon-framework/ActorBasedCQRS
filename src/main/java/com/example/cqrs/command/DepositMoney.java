package com.example.cqrs.command;

import java.math.BigDecimal;

public class DepositMoney extends Command {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5404086989680533946L;
	
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