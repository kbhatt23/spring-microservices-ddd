package com.learning.orderservice.core.valueobjects;

import java.util.UUID;

import com.learning.commondomain.valueobjects.BaseId;

public class TrackingId extends BaseId<UUID>{

	public TrackingId(UUID value) {
		super(value);
	}

}
