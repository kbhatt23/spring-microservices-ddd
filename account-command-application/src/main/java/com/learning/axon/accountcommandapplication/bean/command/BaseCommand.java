package com.learning.axon.accountcommandapplication.bean.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

//common for all the command
public class BaseCommand<T> {
	// need id for the flow from comnandhandler queu to app
	@TargetAggregateIdentifier
	public final T id;

	public BaseCommand(T id) {
		this.id = id;
	}
}
