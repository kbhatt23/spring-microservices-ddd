package com.learning.paymentdomaincore.valueobjects;

import java.util.UUID;

import com.learning.commondomain.valueobjects.BaseId;

public class PaymentId extends BaseId<UUID>{

	public PaymentId(UUID value) {
		super(value);
	}

}
