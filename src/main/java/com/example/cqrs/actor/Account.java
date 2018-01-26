package com.example.cqrs.actor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.cqrs.command.CreateAccount;
import com.example.cqrs.command.DepositMoney;
import com.example.cqrs.command.TakeSnapshot;
import com.example.cqrs.command.WithdrawMoney;
import com.example.cqrs.common.AccountMapper;
import com.example.cqrs.common.AccountState;
import com.example.cqrs.event.AccountCreated;
import com.example.cqrs.event.MoneyDeposited;
import com.example.cqrs.event.MoneyWithdrawn;
import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.readmodel.AccountEntryRepository;

import akka.persistence.AbstractPersistentActor;
import akka.persistence.RecoveryCompleted;

public class Account extends AbstractPersistentActor {
	private static final Logger LOG = LoggerFactory.getLogger(Account.class);
    	
	private UUID id;
	
	private String accountNumber;
	private String accountName;	
	private BigDecimal balance = BigDecimal.ZERO;
	private AccountState state = new AccountState();
	
	private AccountEntryRepository repository;
	
	private AccountMapper mapper = new AccountMapper();
	
	public Account(String accountNumber, String accountName, AccountEntryRepository repository) {
		this.balance = BigDecimal.ZERO;
		repository.findByAccountNumberAndAccountName(accountNumber, accountName)
			.ifPresent(entry -> {
				this.id = entry.getId();
			});
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.repository = repository;
	}
	
	@Override
	public String persistenceId() {
		return accountNumber;
	}

	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(CreateAccount.class, command -> {
				LOG.info("New Account Created");
				persist(new AccountCreated(command.getAccountId(), command.getAccountNumber(), command.getAccountName()), this::applyEvent);
			})
			.match(DepositMoney.class, command -> {
				LOG.info("Deposit " + command.getAmount());
				persist(new MoneyDeposited(command.getAmount()), this::applyEvent);
			})
			.match(WithdrawMoney.class, command -> {
				LOG.info("WithDraw " + command.getAmount());
				persist(new MoneyWithdrawn(command.getAmount()), this::applyEvent);
			})
			.match(TakeSnapshot.class, event -> {
				LOG.info("TakeSnapshot ");
				saveSnapshot(state.copy());
			})
			.matchEquals("print", command -> {
				System.out.println(toString());
			}).build();
	}

	@Override
	public Receive createReceiveRecover() {
		return receiveBuilder().match(AccountCreated.class, event -> {
			state.update(event);
			this.balance = BigDecimal.ZERO;
			this.accountNumber = event.getAccountNumber();
			this.id = event.getAccountId();
			this.accountName = event.getAccountName();
			System.out.println("Replay Event AccountCreated. " + toString());
		}).match(MoneyDeposited.class, event -> {
			state.update(event);
			this.balance = balance.add(event.getAmount());
			System.out.println("Replay Event MoneyDeposited " + event.getAmount() + " " + toString());
		}).match(MoneyWithdrawn.class, event -> {
			state.update(event);
			this.balance = balance.subtract(event.getAmount());
			System.out.println("Replay Event MoneyWithdrawn " + event.getAmount() + " " + toString());
		}).match(RecoveryCompleted.class, e -> {
			System.out.println("Replay Events Completed!...");
		}).build();
	}

	private void applyEvent(Object event) {
		if (event instanceof AccountCreated) {
			this.state.update((AccountCreated) event);
			this.id = ((AccountCreated) event).getAccountId();
			this.accountNumber = ((AccountCreated) event).getAccountNumber();
			this.accountName = ((AccountCreated) event).getAccountName();
			this.balance = BigDecimal.ZERO;
			
			AccountEntry accountEntry = mapper.map(this);
			accountEntry.setId(this.id);
			repository.save(accountEntry);
		}
		if (event instanceof MoneyDeposited) {
			this.state.update((MoneyDeposited) event);
			this.balance = balance.add(((MoneyDeposited) event).getAmount());
			AccountEntry entry = getAccountEntry();
			entry.setBalance(balance);
			repository.save(entry);
		}
		if (event instanceof MoneyWithdrawn) {
			this.state.update((MoneyWithdrawn) event);
			this.balance = balance.subtract(((MoneyWithdrawn) event).getAmount());
			AccountEntry entry = getAccountEntry();
			entry.setBalance(balance);
			repository.save(entry);
		}
		getContext().getSystem().eventStream().publish(event);		
	}
	
	private AccountEntry getAccountEntry() {
		Optional<AccountEntry> maybeAccountEntry = repository.findByAccountNumberAndAccountName(accountNumber, accountName);
		if (maybeAccountEntry.isPresent()) {
			return maybeAccountEntry.get();
		}
		return null;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountName=" + accountName + ", balance=" + balance + ", state="
				+ state + "]";
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}