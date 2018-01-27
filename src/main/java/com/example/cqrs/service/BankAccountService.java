package com.example.cqrs.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cqrs.actor.Account;
import com.example.cqrs.command.CreateAccount;
import com.example.cqrs.command.DepositMoney;
import com.example.cqrs.command.TakeSnapshot;
import com.example.cqrs.command.WithdrawMoney;
import com.example.cqrs.event.store.EventStoreRepository;
import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.readmodel.AccountEntryRepository;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@Service
public class BankAccountService {
	
	ActorSystem system = ActorSystem.create("BankAccountSystem");
	ActorRef persistentActor;
	
	@Autowired
	private AccountEntryRepository repository;
	
	@Autowired
	private EventStoreRepository eventRepository;
	
	public AccountEntry getAccountByAccountNumber(String accountNumber) {
		return repository.findByAccountNumber(accountNumber).orElse(null);
	}
	
	public void createNewBankAccount(String accountNumber, String accountName) {
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository, eventRepository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new CreateAccount(accountNumber, accountName), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());
	}

	public void depositAmount(String accountNumber, String accountName, BigDecimal amount) {
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository, eventRepository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new DepositMoney(amount, accountNumber), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());	
		persistentActor.tell("print", ActorRef.noSender());	
	}
	
	public void withDrawAmount(String accountNumber, String accountName, BigDecimal amount) {
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository, eventRepository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new WithdrawMoney(amount, accountNumber), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());
		persistentActor.tell("print", ActorRef.noSender());
	}

}