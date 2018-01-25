package com.example.cqrs.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.cqrs.event.AccountCreated;
import com.example.cqrs.event.MoneyDeposited;
import com.example.cqrs.event.MoneyWithdrawn;

public class AccountState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7433613989191864094L;
	private List<Object> events;

	public AccountState() {
        this(new ArrayList<>());
    }

	public AccountState(ArrayList<Object> events) {
        this.events = events;
    }

	public AccountState copy() {
		return new AccountState(new ArrayList<>(events));
	}
	
	public void update(Object event) {
		if (event instanceof AccountCreated) {
			events.add((AccountCreated) event);
		}
		if (event instanceof MoneyDeposited) {
			events.add((MoneyDeposited) event);
		}
		if (event instanceof MoneyWithdrawn) {
			events.add((MoneyWithdrawn) event);
		}
	}

	public int size() {
		return events.size();
	}

	@Override
	public String toString() {
		return events.toString();
	}
	
}
