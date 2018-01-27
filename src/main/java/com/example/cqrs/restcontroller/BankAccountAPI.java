package com.example.cqrs.restcontroller;

import java.math.BigDecimal;
import java.util.Map;

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

	private static final String ACCOUNT_NUMBER = "/{accountNumber}";
	private static final String DEPOSIT = "/deposit";
	private static final String WITHDRAW = "/withdraw";

	private static final String ACCOUNT_NUMBER_PARAM = "account_number";
	private static final String ACCOUNT_NAME_PARAM = "account_name";
	private static final String AMOUNT_PARAM = "amount";

	@Autowired
	private BankAccountService bankAccountService;

	@GetMapping(ACCOUNT_NUMBER)
	public AccountEntry getAccountEntryByAccountNumber(@PathVariable String accountNumber) {
		return bankAccountService.getAccountByAccountNumber(accountNumber);
	}

	@PostMapping
	public void createNewAccount(@RequestBody Map<String, String> request) {
		bankAccountService.createNewBankAccount(request.get(ACCOUNT_NUMBER_PARAM), request.get(ACCOUNT_NAME_PARAM));
	}

	@PutMapping(DEPOSIT)
	public void depositAmount(@RequestBody Map<String, String> request) {
		bankAccountService.depositAmount(request.get(ACCOUNT_NUMBER_PARAM), request.get(ACCOUNT_NAME_PARAM),
				new BigDecimal(request.get(AMOUNT_PARAM)));
	}

	@PutMapping(WITHDRAW)
	public void withDrawAmount(@RequestBody Map<String, String> request) {
		bankAccountService.withDrawAmount(request.get(ACCOUNT_NUMBER_PARAM), request.get(ACCOUNT_NAME_PARAM),
				new BigDecimal(request.get(AMOUNT_PARAM)));
	}

}