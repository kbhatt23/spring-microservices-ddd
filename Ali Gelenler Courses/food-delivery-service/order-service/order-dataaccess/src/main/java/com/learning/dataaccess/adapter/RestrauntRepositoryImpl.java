package com.learning.dataaccess.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.learning.commondataaccess.entity.RestrauntEntity;
import com.learning.commondataaccess.entity.RestrauntJPARepository;
import com.learning.dataaccess.helper.RestrauntDataAccessMapper;
import com.learning.orderapplicationservice.ports.output.RestrauntRepository;
import com.learning.orderservice.core.entities.Restraunt;

@Service
public class RestrauntRepositoryImpl implements RestrauntRepository {

	private final RestrauntJPARepository restrauntJPARepository;

	private final RestrauntDataAccessMapper restrauntDataAccessMapper;

	public RestrauntRepositoryImpl(RestrauntJPARepository restrauntJPARepository,
			RestrauntDataAccessMapper restrauntDataAccessMapper) {
		this.restrauntJPARepository = restrauntJPARepository;
		this.restrauntDataAccessMapper = restrauntDataAccessMapper;
	}

	@Override
	public Optional<Restraunt> findRestaurantInformation(Restraunt restaurant) {
		List<UUID> productIds = restrauntDataAccessMapper.findProductIDsFromResraunt(restaurant);
		Optional<List<RestrauntEntity>> restrauntEntities = restrauntJPARepository
				.findByRestrauntIdAndProductIdIn(restaurant.getId().getValue(), productIds);
		
		return restrauntEntities.map(restrauntDataAccessMapper :: createRestrauntFromRestrauntEntities);
	}

}
