package com.learning.paymentdomaincore.valueobjects;

import java.util.UUID;

import com.learning.commondomain.valueobjects.BaseId;

public class CreditEntryId extends BaseId<UUID> {

	public CreditEntryId(UUID value) {
		super(value);
	}

}
