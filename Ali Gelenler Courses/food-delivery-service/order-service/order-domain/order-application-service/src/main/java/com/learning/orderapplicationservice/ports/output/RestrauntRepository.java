package com.learning.orderapplicationservice.ports.output;

import java.util.Optional;

import com.learning.orderservice.core.entities.Restraunt;

public interface RestrauntRepository {

	Optional<Restraunt> findRestaurantInformation(Restraunt restaurant);
}
