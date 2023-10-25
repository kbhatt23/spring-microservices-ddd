package com.learning.paymentdataaccess.mapper;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.paymentdataaccess.entities.CreditEntryEntity;
import com.learning.paymentdomaincore.entities.CreditEntry;
import com.learning.paymentdomaincore.valueobjects.CreditEntryId;

@Component
public class CreditEntryDataMapper {

	public CreditEntryEntity credittoCreditEntity(CreditEntry creditEntry) {
		return CreditEntryEntity.builder().id(creditEntry.getId().getValue())
				.customerId(creditEntry.getCustomerId().getValue())
				.totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount()).build();
	}

	public CreditEntry creditEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
		return new CreditEntry(new CreditEntryId(creditEntryEntity.getId()),
				new CustomerId(creditEntryEntity.getCustomerId()), new Money(creditEntryEntity.getTotalCreditAmount()));
	}
}
