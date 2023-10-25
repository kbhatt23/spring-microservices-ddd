package com.learning.paymentcontainer.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.paymentdomaincore.services.PaymentDomainService;
import com.learning.paymentdomaincore.services.PaymentDomainServiceImpl;

@Configuration
public class BeanConfiguration {

	@Bean
	public PaymentDomainService paymentDomainService() {
		return new PaymentDomainServiceImpl();
	}
}
