package com.learning.customerapplicationservice.ports.output;

import com.learning.customerdomaincore.events.CustomerCreatedEvent;

public interface CustomerMessagePublisher {

	public void publish(CustomerCreatedEvent customerCreatedEvent);
}
