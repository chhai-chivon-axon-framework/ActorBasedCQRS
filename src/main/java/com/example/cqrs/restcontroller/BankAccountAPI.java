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

import com.example.cqrs.readmodel.AccountEntry;
import com.example.cqrs.service.BankAccountService;

@RestController
@RequestMapping(BankAccountAPI.API + BankAccountAPI.BANK_ACCOUNT)
public class BankAccountAPI {

	public static final String API = "/api";
	public static final String BANK_ACCOUNT = "/bankaccounts";
	public static final String ACCOUNT_NUMBER = "/{accountNumber}";
	public static final String DEPOSIT = "/deposit";
	public static final String WITHDRAW = "/withdraw";

	public static final Logger logger = LoggerFactory.getLogger(BankAccountAPI.class);

	@Autowired
	private BankAccountService bankAccountService;

	@GetMapping(ACCOUNT_NUMBER)
	public AccountEntry getAccountEntryByAccountNumber(@PathVariable String accountNumber) {
		return bankAccountService.getAccountByAccountNumber(accountNumber);
	}

	@PostMapping
	public void createNewAccount(@RequestBody Map<String, String> request) {
		bankAccountService.createNewBankAccount(request.get("account_number"), request.get("account_name"));
	}

	@PutMapping(DEPOSIT)
	public void depositAmount(@RequestBody Map<String, String> request) {
		bankAccountService.depositAmount(request.get("account_number"), request.get("account_name"),
				new BigDecimal(request.get("amount")));
	}

	@PutMapping(WITHDRAW)
	public void withDrawAmount(@RequestBody Map<String, String> request) {
		bankAccountService.withDrawAmount(request.get("account_number"), request.get("account_name"),
				new BigDecimal(request.get("amount")));
	}
}
