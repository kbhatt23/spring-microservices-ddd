package com.learning.paymentdomaincore.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.paymentdomaincore.valueobjects.CreditHistoryId;
import com.learning.paymentdomaincore.valueobjects.TransactionType;

public class CreditHistory extends BaseEntity<CreditHistoryId> {

	private final CustomerId customerId;
	private final Money amount;
	private final TransactionType transactionType;

	public CreditHistory(CreditHistoryId id, CustomerId customerId, Money amount, TransactionType transactionType) {
		super(id);
		this.customerId = customerId;
		this.amount = amount;
		this.transactionType = transactionType;
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public Money getAmount() {
		return amount;
	}

	public TransactionType getTransactionType() {
		return transactionType;
	}
}
