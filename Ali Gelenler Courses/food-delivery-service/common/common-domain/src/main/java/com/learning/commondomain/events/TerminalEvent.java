package com.learning.commondomain.events;

public final class TerminalEvent implements DomainEvent<Void> {

	private TerminalEvent() {
	}

	public static final TerminalEvent INSTANCE = new TerminalEvent();

}
