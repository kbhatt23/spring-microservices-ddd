package com.learning.orderapplicationservice.ports.input;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.learning.orderapplicationservice.ports.output.CustomerRepository;
import com.learning.orderapplicationservice.ports.output.OrderCancelledPaymentRequestEventPublisher;
import com.learning.orderapplicationservice.ports.output.OrderCreatedPaymentRequestEventPublisher;
import com.learning.orderapplicationservice.ports.output.OrderPaidRestrauntRequestEventPublisher;
import com.learning.orderapplicationservice.ports.output.OrderRepository;
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
	public OrderCreatedPaymentRequestEventPublisher orderCreatedPaymentRequestEventPublisher() {
		return Mockito.mock(OrderCreatedPaymentRequestEventPublisher.class);
	}
	
	@Bean
	public OrderCancelledPaymentRequestEventPublisher orderCancelledPaymentRequestEventPublisher() {
		return Mockito.mock(OrderCancelledPaymentRequestEventPublisher.class);
	}
	
	@Bean
	public OrderPaidRestrauntRequestEventPublisher orderPaidRestrauntRequestEventPublisher() {
		return Mockito.mock(OrderPaidRestrauntRequestEventPublisher.class);
	}
	
	
}
