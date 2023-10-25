package com.learning.paymentdomaincore.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.paymentdomaincore.valueobjects.CreditEntryId;

public class CreditEntry extends BaseEntity<CreditEntryId> {

	public CreditEntry(CreditEntryId id, CustomerId customerId, Money totalCreditAmount) {
		super(id);
		this.customerId = customerId;
		this.totalCreditAmount = totalCreditAmount;
	}

	private final CustomerId customerId;
	private Money totalCreditAmount;

	public void addCreditAmount(Money amount) {
		totalCreditAmount = totalCreditAmount.add(amount);
	}

	public void subtractCreditAmount(Money amount) {
		totalCreditAmount = totalCreditAmount.substract(amount);
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public Money getTotalCreditAmount() {
		return totalCreditAmount;
	}

}
