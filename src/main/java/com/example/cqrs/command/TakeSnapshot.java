package com.example.cqrs.command;

public class TakeSnapshot extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7919365036072187536L;

	private String accountNumber;
	
	public TakeSnapshot(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	@Override
	public String toString() {
		return "TakeSnapshot [accountNumber=" + accountNumber + "]";
	}
}
