package com.example.cqrs.readmodel;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountEntry {
	
	@Id
	private String accountNumber;
	private String accountName;
	private String createdDate;
	private AccountStatus status;
	private BigDecimal balance;
	
	public AccountEntry(){ }
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "AccountEntry [accountNumber=" + accountNumber + ", accountName=" + accountName + ", createdDate="
				+ createdDate + ", status=" + status + ", balance=" + balance + "]";
	}



	public enum AccountStatus {
		OPENED,
		ACTIVE,
		CLOSED,
	}
}
