package com.learning.orderapplicationservice.ports.input;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.learning.orderapplicationservice.ports.output.ApprovalOutboxRepository;
import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
import com.learning.orderapplicationservice.ports.output.PaymentOutboxRepository;
import com.learning.orderapplicationservice.ports.output.PaymentRequestOutboxMessagePublisher;
import com.learning.orderapplicationservice.ports.output.RestaurantApprovalRequestOutboxMessagePublisher;
import com.learning.orderapplicationservice.ports.output.RestrauntRepository;
import com.learning.orderservice.core.services.OrderDomainService;
import com.learning.orderservice.core.services.OrderDomainServiceImpl;

@SpringBootApplication(scanBasePackages = "com.learning")
public class OrderTestConfiguration {

	@Bean
	public OrderRepository orderRepository() {
		return Mockito.mock(OrderRepository.class);
	}

	@Bean
	public CustomerRepository customerRepository() {
		return Mockito.mock(CustomerRepository.class);
	}

	@Bean
	public RestrauntRepository restaurantRepository() {
		return Mockito.mock(RestrauntRepository.class);
	}

	@Bean
	public OrderDomainService orderDomainService() {
		return new OrderDomainServiceImpl();
	}

	@Bean
	public PaymentOutboxRepository paymentOutboxRepository() {
		return Mockito.mock(PaymentOutboxRepository.class);
	}

	@Bean
	public ApprovalOutboxRepository approvalOutboxRepository() {
		return Mockito.mock(ApprovalOutboxRepository.class);
	}

	@Bean
	public PaymentRequestOutboxMessagePublisher paymentRequestMessagePublisher() {
		return Mockito.mock(PaymentRequestOutboxMessagePublisher.class);
	}

	@Bean
	public RestaurantApprovalRequestOutboxMessagePublisher restaurantApprovalRequestMessagePublisher() {
		return Mockito.mock(RestaurantApprovalRequestOutboxMessagePublisher.class);
	}
}
