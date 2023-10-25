package com.learning.paymentapplicationservice.output;

import java.util.Optional;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.paymentdomaincore.entities.CreditEntry;

public interface CreditEntryRepository {

	CreditEntry save(CreditEntry creditEntry);

	Optional<CreditEntry> findByCustomerId(CustomerId customerId);
}
