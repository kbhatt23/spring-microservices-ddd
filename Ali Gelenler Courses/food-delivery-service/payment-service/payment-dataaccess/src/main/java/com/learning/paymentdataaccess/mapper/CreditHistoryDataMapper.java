package com.learning.paymentdataaccess.mapper;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.paymentdataaccess.entities.CreditHistoryEntity;
import com.learning.paymentdomaincore.entities.CreditHistory;
import com.learning.paymentdomaincore.valueobjects.CreditHistoryId;

@Component
public class CreditHistoryDataMapper {

	public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
		return new CreditHistory(new CreditHistoryId(creditHistoryEntity.getId()),
				new CustomerId(creditHistoryEntity.getCustomerId()), new Money(creditHistoryEntity.getAmount()),
				creditHistoryEntity.getType());

	}

	public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
		return CreditHistoryEntity.builder().id(creditHistory.getId().getValue())
				.customerId(creditHistory.getCustomerId().getValue()).amount(creditHistory.getAmount().getAmount())
				.type(creditHistory.getTransactionType()).build();
	}
}
