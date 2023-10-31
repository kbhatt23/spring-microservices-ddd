package com.learning.saga;

public enum SagaStatus {
	STARTED, FAILED, SUCCEEDED, PROCESSING, COMPENSATING, COMPENSATED
}
