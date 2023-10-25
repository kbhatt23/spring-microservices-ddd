package com.learning.paymentapplicationservice.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.events.PaymentFailedEvent;

public interface PaymentFailedMessagePublisher extends DomainEventPublisher<PaymentFailedEvent>{

}
