package com.learning.restrauntdomaincore.valueobjects;

import java.util.UUID;

import com.learning.commondomain.valueobjects.BaseId;

public class OrderApprovalId extends BaseId<UUID> {

	public OrderApprovalId(UUID value) {
		super(value);
	}

}
