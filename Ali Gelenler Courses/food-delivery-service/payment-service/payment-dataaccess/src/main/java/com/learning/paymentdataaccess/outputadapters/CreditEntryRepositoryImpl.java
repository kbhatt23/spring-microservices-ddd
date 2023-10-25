package com.learning.paymentdataaccess.outputadapters;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.paymentapplicationservice.output.CreditEntryRepository;
import com.learning.paymentdataaccess.mapper.CreditEntryDataMapper;
import com.learning.paymentdataaccess.repositories.CreditEntryJPARepository;
import com.learning.paymentdomaincore.entities.CreditEntry;

@Component
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

	private final CreditEntryDataMapper creditEntryDataMapper;

	private final CreditEntryJPARepository creditEntryJPARepository;

	public CreditEntryRepositoryImpl(CreditEntryDataMapper creditEntryDataMapper,
			CreditEntryJPARepository creditEntryJPARepository) {
		this.creditEntryDataMapper = creditEntryDataMapper;
		this.creditEntryJPARepository = creditEntryJPARepository;
	}

	@Override
	public CreditEntry save(CreditEntry creditEntry) {
		return creditEntryDataMapper.creditEntityToCreditEntry(
				creditEntryJPARepository.save(creditEntryDataMapper.credittoCreditEntity(creditEntry)));
	}

	@Override
	public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
		return creditEntryJPARepository.findByCustomerId(customerId.getValue())
		.map(creditEntryDataMapper :: creditEntityToCreditEntry);
	}

}
