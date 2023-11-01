package com.learning.customercontainer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.customerdomaincore.services.CustomerDomainService;
import com.learning.customerdomaincore.services.CustomerDomainServiceImpl;

@Configuration
public class BeanConfiguration {

	@Bean
	public CustomerDomainService customerDomainService() {
		return new CustomerDomainServiceImpl();
	}
}
