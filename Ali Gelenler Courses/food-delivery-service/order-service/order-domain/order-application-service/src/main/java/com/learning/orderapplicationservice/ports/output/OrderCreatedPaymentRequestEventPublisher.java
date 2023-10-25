package com.learning.orderapplicationservice.ports.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.events.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestEventPublisher extends DomainEventPublisher<OrderCreatedEvent>{

}
