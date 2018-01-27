package com.example.cqrs.actor;

import java.io.IOException;
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
import com.example.cqrs.common.PayloadUtils;
import com.example.cqrs.event.AccountCreated;
import com.example.cqrs.event.MoneyDeposited;
import com.example.cqrs.event.MoneyWithdrawn;
import com.example.cqrs.event.SnapshotTaken;
import com.example.cqrs.event.store.EventStore;
import com.example.cqrs.event.store.EventStoreRepository;
import com.example.cqrs.event.store.SnapshotEventStore;
import com.example.cqrs.event.store.SnapshotEventStoreRepository;
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
	
	private final AccountEntryRepository repository;
	private final EventStoreRepository eventRepository;
	private final SnapshotEventStoreRepository snapshotRepository;
	
	private AccountMapper mapper = new AccountMapper();
	Optional<EventStore> latestEvent = Optional.empty();
	
	public Account(String accountNumber, String accountName, AccountEntryRepository repository, EventStoreRepository eventRepository, SnapshotEventStoreRepository snapshotRepository) {
		this.balance = BigDecimal.ZERO;
		repository.findByAccountNumberAndAccountName(accountNumber, accountName)
			.ifPresent(entry -> {
				this.id = entry.getId();
			});
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.repository = repository;
		this.eventRepository = eventRepository;
		this.snapshotRepository = snapshotRepository;
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
				LOG.info("Deposit " + command.getAmount() + " to account " + command.getAccountNumber());
				populateAggreate(command.getAccountNumber());
				persist(new MoneyDeposited(command.getAmount(), command.getAccountNumber()), this::applyEvent);
			})
			.match(WithdrawMoney.class, command -> {
				LOG.info("WithDraw " + command.getAmount() + " from account " + command.getAccountNumber());
				populateAggreate(command.getAccountNumber());
				persist(new MoneyWithdrawn(command.getAmount(), command.getAccountNumber()), this::applyEvent);
			})
			.match(TakeSnapshot.class, event -> {
				LOG.info("TakeSnapshot ");
				saveSnapshot(state.copy());
				applyEvent(event);
			})
			.matchEquals("print", command -> {
				System.out.println(toString());
			}).build();
	}

	private void populateAggreate(String accountNumber) {
		repository.findByAccountNumber(accountNumber)
			.ifPresent(entry -> {
				this.id = entry.getId();
				this.accountName = entry.getAccountName();
				this.accountNumber = accountNumber;
				this.balance = entry.getBalance();
			});
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
	
	private void applyEvent(Object event) throws IOException {
		EventStore eventStore = new EventStore();
		if (event instanceof AccountCreated) {
			this.state.update((AccountCreated) event);
			this.id = ((AccountCreated) event).getAccountId();
			this.accountNumber = ((AccountCreated) event).getAccountNumber();
			this.accountName = ((AccountCreated) event).getAccountName();
			this.balance = BigDecimal.ZERO;
			
			eventStore.setPayload(PayloadUtils.serialize((AccountCreated) event));
			eventStore.setPayloadType(AccountCreated.class.getCanonicalName());
			
			AccountEntry accountEntry = mapper.map(this);
			accountEntry.setId(this.id);
			repository.save(accountEntry);
		}
		if (event instanceof MoneyDeposited) {
			this.state.update((MoneyDeposited) event);
			this.accountNumber = ((MoneyDeposited) event).getAccountNumber();
			this.balance = balance.add(((MoneyDeposited) event).getAmount());
			
			eventStore.setPayload(PayloadUtils.serialize((MoneyDeposited) event));
			eventStore.setPayloadType(MoneyDeposited.class.getCanonicalName());
			
			AccountEntry entry = getAccountEntry();
			entry.setBalance(balance);
			repository.save(entry);
		}
		if (event instanceof MoneyWithdrawn) {
			this.state.update((MoneyWithdrawn) event);
			this.accountNumber = ((MoneyWithdrawn) event).getAccountNumber();
			this.balance = balance.subtract(((MoneyWithdrawn) event).getAmount());
			
			eventStore.setPayload(PayloadUtils.serialize((MoneyWithdrawn) event));
			eventStore.setPayloadType(MoneyWithdrawn.class.getCanonicalName());
			
			AccountEntry entry = getAccountEntry();
			entry.setBalance(balance);
			repository.save(entry);
		}
		if (event instanceof SnapshotTaken) {
			String accountNumber = ((SnapshotTaken) event).getAccountNumber();
			AccountEntry snapShotObject = repository.findByAccountNumber(accountNumber).orElse(null);
			Optional<EventStore> latestEventStore = eventRepository.findTop1ByAggregateIdOrderBySequenceDesc(snapShotObject.getId());
			if (snapShotObject != null && latestEventStore.isPresent()) {
				SnapshotEventStore snapshot = snapshotRepository.findByAggregateId(snapShotObject.getId()).orElse(null);
				if (snapshot == null) {
					snapshot = new SnapshotEventStore();
					snapshot.setId(UUID.randomUUID());
				}
				
				snapshot.setSequence(latestEventStore.get().getSequence());
				snapshot.setAggregateId(snapShotObject.getId());
				snapshot.setPayload(PayloadUtils.serialize(snapShotObject));
				snapshot.setPayloadType(AccountEntry.class.getCanonicalName());
				
				snapshotRepository.save(snapshot);
			}
		}
		
		latestEvent = eventRepository.findTop1ByAggregateIdOrderBySequenceDesc(this.id);
		if (latestEvent.isPresent()) {
			eventStore.setSequence(latestEvent.get().getSequence() + 1);
		} else {
			eventStore.setSequence(Long.valueOf(0));
		}
		eventStore.setAggregateId(this.id);
		eventRepository.save(eventStore);
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
