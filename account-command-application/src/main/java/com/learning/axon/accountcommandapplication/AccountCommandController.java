package com.learning.axon.accountcommandapplication;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.axon.accountcommandapplication.bean.dto.AccountCreateDTO;
import com.learning.axon.accountcommandapplication.bean.dto.MoneyCreditDTO;
import com.learning.axon.accountcommandapplication.bean.dto.MoneyDebitDTO;

@RestController
@RequestMapping("/accounts")
public class AccountCommandController {

	@Autowired
	private AccountCommandService accountCommandService;
	@PostMapping
	public CompletableFuture<String> createAccount(@RequestBody AccountCreateDTO accountCreateDTO){
		
		return accountCommandService.createAccount(accountCreateDTO);
	}
	
	@PutMapping("/credit")
	public CompletableFuture<String> createAccount(@RequestBody MoneyCreditDTO accountCreditDTO){
		return accountCommandService.creditMoneyToAccount(accountCreditDTO.getAccountNumber(), accountCreditDTO);
	}
	@PutMapping("/debit")
	public CompletableFuture<String> createAccount(@RequestBody MoneyDebitDTO moneyDebitDTO){
		return accountCommandService.debitMoneyFromAccount(moneyDebitDTO.getAccountNumber(), moneyDebitDTO);
	}
	
}
