package com.learning.restrauntapplicationservice.output;

import java.util.Optional;

import com.learning.restrauntdomaincore.entities.Restraunt;

public interface RestrauntRepository {

	public Optional<Restraunt> findRestaurantInformation(Restraunt restraunt);
}
