package com.learning.axon.accountqueryapplication.service;

import java.util.List;
import java.util.stream.Collectors;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.stereotype.Service;

import com.learning.axon.accountqueryapplication.bean.entity.AccountQueryEntity;
import com.learning.axon.accountqueryapplication.repostiory.AccountRepository;

@Service
public class AccountQueryServiceImpl {

    private final EventStore eventStore;

    private final AccountRepository accountRepository;

    public AccountQueryServiceImpl(EventStore eventStore, AccountRepository accountRepository) {
        this.eventStore = eventStore;
        this.accountRepository = accountRepository;
    }

    public List<Object> listEventsForAccount(String accountNumber) {
        return eventStore.readEvents(accountNumber).asStream().map( s -> s.getPayload()).collect(Collectors.toList());
    }

    public AccountQueryEntity getAccount(String accountNumber) {
        return accountRepository.findById(accountNumber).get();
    }
    
    public Iterable<AccountQueryEntity> queryAll(){
    	return accountRepository.findAll();
    }
}
