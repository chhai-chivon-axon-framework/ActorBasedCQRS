package com.example.cqrs.event;

import java.math.BigDecimal;

public class MoneyDeposited extends Event {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6794109229414985744L;
	
	private BigDecimal amount;
	
	public MoneyDeposited(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return "MoneyDeposited [amount=" + amount + "]";
	}

}
