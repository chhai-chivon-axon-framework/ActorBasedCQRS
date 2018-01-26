package com.example.cqrs.common;

public class BankAccount {
	
	private String userName;
	private String number;
	
	public BankAccount() { }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "BankAccount [userName=" + userName + ", number=" + number + "]";
	}
	
}
