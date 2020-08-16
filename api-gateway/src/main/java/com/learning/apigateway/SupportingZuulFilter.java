package com.learning.apigateway;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class SupportingZuulFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		// we can get request object and use that to filter out if the run method needs
		// to be executed
		// if returns false it will go to next filter
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		//putting request url object just for demonstration so that other filter can use it
		String requestURL = (String) ctx.get("requestURL");
		System.err.println("jai shree ram from supporting filter using requestURL "+requestURL);
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
