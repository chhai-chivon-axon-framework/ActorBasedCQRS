package com.example.cqrs.restcontroller;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.actor.Account;
import com.example.cqrs.command.CreateAccount;
import com.example.cqrs.command.DepositMoney;
import com.example.cqrs.command.TakeSnapshot;
import com.example.cqrs.command.WithdrawMoney;
import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.readmodel.AccountEntryRepository;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

@RestController
@RequestMapping(BankAccountRestController.API + BankAccountRestController.BANK_ACCOUNT)
public class BankAccountRestController {
	
	public static final String API = "/api";
	public static final String BANK_ACCOUNT = "/bankaccounts";
	public static final String ACCOUNT_NUMBER = "/{accountNumber}";
	public static final String DEPOSIT = "/deposit";
	public static final String WITHDRAW = "/withdraw";
	
	public static final Logger logger = LoggerFactory.getLogger(BankAccountRestController.class);
	
	ActorSystem system = ActorSystem.create("BankAccountSystem");
	ActorRef persistentActor;

	@Autowired
	private AccountEntryRepository repository;
	
	@GetMapping(ACCOUNT_NUMBER) 
    public AccountEntry getAccountEntryByAccountNumber(@PathVariable String accountNumber) {
		return repository.findByAccountNumber(accountNumber).orElse(null);
	}
	
	@PostMapping
	public void createNewAccount(@RequestBody Map<String, String> request) {
		String accountName = request.get("account_name");
		String accountNumber = request.get("account_number");				
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new CreateAccount(accountNumber, accountName), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());
	}
	
	@PutMapping(DEPOSIT)
	public void depositAmount(@RequestBody Map<String, String> request) {
		String accountName = request.get("account_name");
		String amount = request.get("amount");
		String accountNumber = request.get("account_number");				
		
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new DepositMoney(new BigDecimal(amount)), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());	
		persistentActor.tell("print", ActorRef.noSender());	
	}
	
	@PutMapping(WITHDRAW)
	public void withDrawAmount(@RequestBody Map<String, String> request) {
		String accountName = request.get("account_name");
		String accountNumber = request.get("account_number");	
		String amount = request.get("amount");
		
		persistentActor = system
				.actorOf(Props.create(Account.class, () -> new Account(accountNumber, accountName, repository)));
		persistentActor.tell("print", ActorRef.noSender());
		persistentActor.tell(new WithdrawMoney(new BigDecimal(amount)), ActorRef.noSender());
		persistentActor.tell(new TakeSnapshot(), ActorRef.noSender());
		persistentActor.tell("print", ActorRef.noSender());	
	}
}
