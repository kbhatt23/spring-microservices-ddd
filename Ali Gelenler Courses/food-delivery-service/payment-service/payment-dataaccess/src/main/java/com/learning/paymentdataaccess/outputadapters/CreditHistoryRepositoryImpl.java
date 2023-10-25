package com.learning.paymentdataaccess.outputadapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.paymentapplicationservice.output.CreditHistoryRepository;
import com.learning.paymentdataaccess.mapper.CreditHistoryDataMapper;
import com.learning.paymentdataaccess.repositories.CreditHistoryJPARepository;
import com.learning.paymentdomaincore.entities.CreditHistory;

@Component
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

	private final CreditHistoryDataMapper creditHistoryDataMapper;

	private final CreditHistoryJPARepository creditHistoryJPARepository;

	public CreditHistoryRepositoryImpl(CreditHistoryDataMapper creditHistoryDataMapper,
			CreditHistoryJPARepository creditHistoryJPARepository) {
		this.creditHistoryDataMapper = creditHistoryDataMapper;
		this.creditHistoryJPARepository = creditHistoryJPARepository;
	}

	@Override
	public CreditHistory save(CreditHistory creditHistory) {
		return creditHistoryDataMapper.creditHistoryEntityToCreditHistory(creditHistoryJPARepository
				.save(creditHistoryDataMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
	}

	@Override
	public Optional<List<CreditHistory>> findByCustomerId(CustomerId customerId) {
		return creditHistoryJPARepository.findByCustomerId(customerId.getValue())
				.map(histories -> histories.stream()
						.map(creditHistoryDataMapper::creditHistoryEntityToCreditHistory).collect(Collectors.toList())
				);

	}

};