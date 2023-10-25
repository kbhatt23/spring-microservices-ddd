package com.learning.dataaccess.helper;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondataaccess.entity.RestrauntEntity;
import com.learning.commondataaccess.exception.RestaurantDataAccessException;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.orderservice.core.entities.Product;
import com.learning.orderservice.core.entities.Restraunt;

@Component
public class RestrauntDataAccessMapper {

	public List<UUID> findProductIDsFromResraunt(Restraunt restraunt) {
		return restraunt.getProductsMap().entrySet().stream().map(entry -> entry.getValue().getId().getValue())
				.collect(Collectors.toList());
	}

	public Restraunt createRestrauntFromRestrauntEntities(List<RestrauntEntity> restrauntEntities) {
		RestrauntEntity restrauntEntityFirst = restrauntEntities.stream().findFirst()
				.orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!"));

		Map<ProductId, Product> productMap = restrauntEntities.stream()
		                 .map(entity -> new Product(new ProductId(entity.getProductId()), 
		                		 entity.getProductName(), new Money(entity.getProductPrice())))
		                 .collect(Collectors.toMap(Product :: getId, Function.identity()));
		
		return new Restraunt(new RestrauntId(restrauntEntityFirst.getRestrauntId()), productMap, restrauntEntityFirst.getActive());
	}
}
