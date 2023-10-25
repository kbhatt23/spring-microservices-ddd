package com.learning.restrauntdataaccess.adapter;

import org.springframework.stereotype.Service;

import com.learning.restrauntapplicationservice.output.OrderApprovalRepository;
import com.learning.restrauntdataaccess.mapper.RestrauntDataAccessMapper;
import com.learning.restrauntdataaccess.repositories.OrderApprovalJpaRepository;
import com.learning.restrauntdomaincore.entities.OrderApproval;

@Service
public class OrderApprovalRepositoryImpl implements OrderApprovalRepository {

	private final OrderApprovalJpaRepository orderApprovalJpaRepository;

	private final RestrauntDataAccessMapper restrauntDataAccessMapper;

	public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository,
			RestrauntDataAccessMapper restrauntDataAccessMapper) {
		super();
		this.orderApprovalJpaRepository = orderApprovalJpaRepository;
		this.restrauntDataAccessMapper = restrauntDataAccessMapper;
	}

	@Override
	public OrderApproval save(OrderApproval orderApproval) {
		return restrauntDataAccessMapper.orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
				.save(restrauntDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
	}

}
