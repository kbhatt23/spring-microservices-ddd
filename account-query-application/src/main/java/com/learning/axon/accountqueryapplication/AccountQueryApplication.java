package com.learning.axon.accountqueryapplication;

import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.learning.axon.accountqueryapplication.bean.aggregates.AccountAggregate;

@SpringBootApplication
@EnableJpaRepositories
public class AccountQueryApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountQueryApplication.class, args);
	}
	  @Bean
	    EventSourcingRepository<AccountAggregate> accountAggregateEventSourcingRepository(EventStore eventStore){
	        EventSourcingRepository<AccountAggregate> repository = EventSourcingRepository.builder(AccountAggregate.class).eventStore(eventStore).build();
	        return repository;
	    }
}
