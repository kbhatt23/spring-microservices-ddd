package com.learning.commondomain.valueobjects;

import java.util.UUID;

public class OrderId extends BaseId<UUID>{

	public OrderId(UUID value) {
		super(value);
	}

}
