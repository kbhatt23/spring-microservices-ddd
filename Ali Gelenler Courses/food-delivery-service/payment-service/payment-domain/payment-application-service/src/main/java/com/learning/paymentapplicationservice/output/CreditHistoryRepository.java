package com.learning.paymentapplicationservice.output;

import java.util.List;
import java.util.Optional;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.paymentdomaincore.entities.CreditHistory;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId);
}
