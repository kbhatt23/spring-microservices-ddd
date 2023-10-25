package com.learning.paymentdomaincore.valueobjects;

import java.util.UUID;

import com.learning.commondomain.valueobjects.BaseId;

public class CreditHistoryId extends BaseId<UUID> {

	public CreditHistoryId(UUID value) {
		super(value);
	}

}
