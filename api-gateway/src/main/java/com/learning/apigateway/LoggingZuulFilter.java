package com.learning.apigateway;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class LoggingZuulFilter extends ZuulFilter {

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
		HttpServletRequest request = ctx.getRequest();
		//putting request url object just for demonstration so that other filter can use it
		ctx.put("requestURL", request.getRequestURL().toString());
		System.err.println(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 0;
	}

}
