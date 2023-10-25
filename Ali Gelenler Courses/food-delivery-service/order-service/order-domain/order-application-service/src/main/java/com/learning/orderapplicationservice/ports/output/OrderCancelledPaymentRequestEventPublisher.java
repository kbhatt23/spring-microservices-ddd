package com.learning.orderapplicationservice.ports.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.orderservice.core.events.OrderCancelledEvent;

public interface OrderCancelledPaymentRequestEventPublisher extends DomainEventPublisher<OrderCancelledEvent>{

}
