package com.learning.restrauntcontainer.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.learning.restrauntdomaincore.services.RestrauntDomainService;
import com.learning.restrauntdomaincore.services.RestrauntDomainServiceImpl;

@Configuration
public class BeanConfiguration {

	@Bean
	public RestrauntDomainService restrauntDomainService() {
		return new RestrauntDomainServiceImpl();
	}
}
