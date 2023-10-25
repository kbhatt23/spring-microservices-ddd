package com.learning.restrauntapplicationservice.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.restrauntdomaincore.events.OrderRejectedEvent;

public interface OrderRejectedMessagePublisher extends DomainEventPublisher<OrderRejectedEvent>{

}
