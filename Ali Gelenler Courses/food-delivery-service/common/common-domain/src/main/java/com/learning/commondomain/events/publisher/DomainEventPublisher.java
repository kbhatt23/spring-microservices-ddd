package com.learning.commondomain.events.publisher;

import com.learning.commondomain.events.DomainEvent;

public interface DomainEventPublisher<T extends DomainEvent> {

	void publish(T domainEvent);
}
