package com.learning.saga;

/*T represents data in messaging listener that is recieved
S represents succesful transaction event
V represents failure transaction event*/

public interface SagaStep<T> {

	void process(T data);

	void rollback(T data);
}
