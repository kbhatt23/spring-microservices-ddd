package com.learning.saga;

import com.learning.commondomain.events.DomainEvent;

/*T represents data in messaging listener that is recieved
S represents succesful transaction event
V represents failure transaction event*/


public interface SagaStep<T, S extends DomainEvent, V extends DomainEvent> {

	S process(T data);
	
	V rollback(T data);
}
