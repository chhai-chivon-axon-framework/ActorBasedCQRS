package com.example.cqrs.event;

import java.math.BigDecimal;

public class MoneyWithdrawn extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7876662770710605707L;
	
	private BigDecimal amount;
	
	public MoneyWithdrawn(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "MoneyWithdrawn [amount=" + amount + "]";
	}
	
}
