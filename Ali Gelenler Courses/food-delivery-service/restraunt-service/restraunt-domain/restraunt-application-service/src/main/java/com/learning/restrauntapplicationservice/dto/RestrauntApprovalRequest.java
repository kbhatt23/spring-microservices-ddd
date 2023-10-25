package com.learning.restrauntapplicationservice.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.learning.commondomain.valueobjects.RestaurantOrderStatus;
import com.learning.restrauntdomaincore.entities.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestrauntApprovalRequest {
	private String id;
	private String sagaId;
	private String restaurantId;
	private String orderId;
	private RestaurantOrderStatus restaurantOrderStatus;
	private List<Product> products;
	private BigDecimal price;
	private Instant createdAt;
}
