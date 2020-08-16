package com.learning.cloudstream.streamconsumer.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//@RibbonClient(name = "fake-generator-service", configuration = RibbonConfiguration.class)
@FeignClient(name = "fake-generator-service")
public interface IdGeneratorFeign {

	@GetMapping("/generate")
	public int generateGreetingId();		
	
}
