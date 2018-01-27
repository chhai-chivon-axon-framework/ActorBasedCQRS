package com.example.cqrs.event;

public class SnapshotTaken extends Event {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3771204623802980727L;
	
	private String accountNumber;
	
	public SnapshotTaken(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	@Override
	public String toString() {
		return "SnapshotTaken [accountNumber=" + accountNumber + "]";
	}
	
}