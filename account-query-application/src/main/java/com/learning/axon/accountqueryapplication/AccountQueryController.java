package com.learning.axon.accountqueryapplication;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.learning.axon.accountqueryapplication.bean.entity.AccountQueryEntity;
import com.learning.axon.accountqueryapplication.service.AccountQueryServiceImpl;

@RestController
@RequestMapping("/query-account")
public class AccountQueryController {

	@Autowired
	private AccountQueryServiceImpl accountQueryService;
	
	 @GetMapping("/{accountNumber}")
    public AccountQueryEntity getAccount(@PathVariable(value = "accountNumber") String accountNumber){
        return accountQueryService.getAccount(accountNumber);
    }

    @GetMapping("/{accountNumber}/events")
    public List<Object> listEventsForAccount(@PathVariable(value = "accountNumber") String accountNumber){
        return accountQueryService.listEventsForAccount(accountNumber);
    }
    @GetMapping
    public Iterable<AccountQueryEntity> findAllAccounts(){
    	return accountQueryService.queryAll();
    }
}
