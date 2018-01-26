package com.example.cqrs.readmodel;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class AccountEntry {
	
	@Id
	private UUID id;
	private String accountNumber;
	private String accountName;
	
	@Enumerated(EnumType.STRING)
	private AccountStatus status;
	
	private BigDecimal balance;
	
	public AccountEntry(){ 
		this.status = AccountStatus.OPENED;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
		return "AccountEntry [id=" + id + ", accountNumber=" + accountNumber + ", accountName="
				+ accountName + ", status=" + status + ", balance=" + balance + "]";
	}

	public enum AccountStatus {
		OPENED,
		ACTIVE,
		CLOSED,
	}
}
