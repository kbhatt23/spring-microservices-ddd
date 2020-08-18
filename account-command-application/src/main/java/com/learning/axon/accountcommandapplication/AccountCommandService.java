package com.learning.axon.accountcommandapplication;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learning.axon.accountcommandapplication.bean.command.CreateAccountCommand;
import com.learning.axon.accountcommandapplication.bean.command.CreditMoneyCommand;
import com.learning.axon.accountcommandapplication.bean.command.DebitMoneyCommand;
import com.learning.axon.accountcommandapplication.bean.dto.AccountCreateDTO;
import com.learning.axon.accountcommandapplication.bean.dto.MoneyCreditDTO;
import com.learning.axon.accountcommandapplication.bean.dto.MoneyDebitDTO;

@Service
public class AccountCommandService {
	@Autowired
	private CommandGateway commandGateway;

	public CompletableFuture<String> createAccount(AccountCreateDTO accountCreateDTO) {
		return commandGateway.send(new CreateAccountCommand(UUID.randomUUID().toString(),
				accountCreateDTO.getStartingBalance(), accountCreateDTO.getCurrency()));
	}

	public CompletableFuture<String> creditMoneyToAccount(String accountNumber, MoneyCreditDTO moneyCreditDTO) {
		return commandGateway.send(
				new CreditMoneyCommand(accountNumber, moneyCreditDTO.getCreditAmount(), moneyCreditDTO.getCurrency()));
	}

	public CompletableFuture<String> debitMoneyFromAccount(String accountNumber, MoneyDebitDTO moneyDebitDTO) {
		return commandGateway.send(
				new DebitMoneyCommand(accountNumber, moneyDebitDTO.getDebitAmount(), moneyDebitDTO.getCurrency()));
	}

}
