package com.learning.outbox;

public interface OutboxScheduler {
	void processOutboxMessage();
}
