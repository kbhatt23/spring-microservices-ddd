package com.learning.restrauntapplicationservice.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.restrauntdomaincore.events.OrderApprovedEvent;

public interface OrderApprovedMessagePublisher extends DomainEventPublisher<OrderApprovedEvent>{

}
