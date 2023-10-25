package com.learning.paymentapplicationservice.output;

import com.learning.commondomain.events.publisher.DomainEventPublisher;
import com.learning.paymentdomaincore.events.PaymentCancelledEvent;

public interface PaymentCancelledMessagePublisher extends DomainEventPublisher<PaymentCancelledEvent>{

}
