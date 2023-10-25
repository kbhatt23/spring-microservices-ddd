package com.learning.paymentapplicationservice.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.events.PaymentCompletedEvent;

public interface PaymentCompletedMessagePublisher extends DomainEventPublisher<PaymentCompletedEvent>{

}
