package com.learning.ordercontainer.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.orderservice.core.services.OrderDomainService;
import com.learning.orderservice.core.services.OrderDomainServiceImpl;

@Configuration
public class OrderBeanConfiguration {

	@Bean
	public OrderDomainService orderDomainService() {
		return new OrderDomainServiceImpl();
	}
}
