package com.learning.restrauntdataaccess.mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondataaccess.entity.RestrauntEntity;
import com.learning.commondataaccess.exception.RestaurantDataAccessException;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntdataaccess.entities.OrderApprovalEntity;
import com.learning.restrauntdomaincore.entities.OrderApproval;
import com.learning.restrauntdomaincore.entities.OrderDetail;
import com.learning.restrauntdomaincore.entities.Product;
import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.valueobjects.OrderApprovalId;

@Component
public class RestrauntDataAccessMapper {

	public List<UUID> findProductIDsFromResraunt(Restraunt restaurant) {
		return restaurant.getOrderDetail().getProducts().stream().map(product -> product.getId().getValue())
				.collect(Collectors.toList());
	}

	public Restraunt createRestrauntFromRestrauntEntities(List<RestrauntEntity> restaurantEntities) {
		RestrauntEntity restaurantEntity = restaurantEntities.stream().findFirst()
				.orElseThrow(() -> new RestaurantDataAccessException("No restaurants found!"));

		List<Product> restaurantProducts = restaurantEntities.stream()
				.map(entity -> new Product(new ProductId(entity.getProductId()), entity.getProductName(),
						new Money(entity.getProductPrice()), 0, entity.getProductInStock()))
				.collect(Collectors.toList());

		return new Restraunt(new RestrauntId(restaurantEntity.getRestrauntId()), null, restaurantEntity.getActive(),
				new OrderDetail(null, null, null, restaurantProducts));

	}

	public OrderApprovalEntity orderApprovalToOrderApprovalEntity(OrderApproval orderApproval) {
		return OrderApprovalEntity.builder().id(orderApproval.getId().getValue())
				.restaurantId(orderApproval.getRestrauntId().getValue()).orderId(orderApproval.getOrderId().getValue())
				.status(orderApproval.getOrderApprovalStatus()).build();
	}

	public OrderApproval orderApprovalEntityToOrderApproval(OrderApprovalEntity orderApprovalEntity) {
		return new OrderApproval(new OrderApprovalId(orderApprovalEntity.getId()),
				new RestrauntId(orderApprovalEntity.getRestaurantId()), new OrderId(orderApprovalEntity.getOrderId()),
				orderApprovalEntity.getStatus());
	}
}