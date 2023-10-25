package com.learning.dataaccess.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.learning.commonapplication.constants.CommonConstants;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.dataaccess.entity.OrderAddressEntity;
import com.learning.dataaccess.entity.OrderEntity;
import com.learning.dataaccess.entity.OrderItemEntity;
import com.learning.orderservice.core.entities.Order;
import com.learning.orderservice.core.entities.OrderItem;
import com.learning.orderservice.core.entities.Product;
import com.learning.orderservice.core.valueobjects.OrderItemId;
import com.learning.orderservice.core.valueobjects.StreetAddress;
import com.learning.orderservice.core.valueobjects.TrackingId;

@Component
public class OrderDataAccessMapper {

	public OrderEntity createOrderEntityFromOrderDomain(Order order) {
		 OrderEntity orderEntity = OrderEntity
				 .builder()
				 .id(order.getId().getValue())
				 .customerId(order.getCustomerId().getValue())
				 .restrauntId(order.getRestrauntId().getValue())
				 .trackingId(order.getTrackingId().getValue())
				 .orderStatus(order.getOrderStatus())
				 .orderAddressEntity(createAddressEntityFromAddressDomain(order.getStreetAddress()))
				 .items(createOrderItemEntityFromOrderDomain(order.getItems()))
				 .failureMessages(order.getErrors() != null ?  String.join(CommonConstants.COMMA_DELIMETER, order.getErrors())
						  : CommonConstants.EMPTY_STRING)
				 .price(order.getPrice().getAmount())
				 .build();
		
		 orderEntity.getOrderAddressEntity().setOrder(orderEntity);
		 orderEntity.getItems().forEach(item -> item.setOrder(orderEntity));
		 
		return orderEntity;
	}

	public List<OrderItemEntity> createOrderItemEntityFromOrderDomain(List<OrderItem> items) {
		return items.stream()
		   .map(item -> OrderItemEntity.builder()
				   .id(item.getId().getValue())
				   .productId(item.getProduct().getId().getValue())
				   .quantity(item.getQuantity())
				   .unitPrice(item.getUnitPrice().getAmount())
				   .totalPrice(item.getTotalPrice().getAmount())
				   .build())
		   .collect(Collectors.toList());
	}
	
	public OrderAddressEntity createAddressEntityFromAddressDomain(StreetAddress streetAddress) {
		return OrderAddressEntity
				  .builder()
				  .id(streetAddress.getId())
				  .city(streetAddress.getCity())
				  .street(streetAddress.getStreet())
				  .postalCode(streetAddress.getPostalCode())
				  .build();
	} 
	
	public Order createOrderFromOrderEntity(OrderEntity orderEntity) {

		Order order = new Order(new OrderId(orderEntity.getId()), new CustomerId(orderEntity.getCustomerId()),
				new RestrauntId(orderEntity.getRestrauntId()),
				createDomainAddressFromOrderEntity(orderEntity.getOrderAddressEntity()),
				new Money(orderEntity.getPrice()),
				createOrderItemDomainFromEntity(orderEntity.getItems()));
		
		order.setTrackingId(new TrackingId(orderEntity.getTrackingId()));
		order.setOrderStatus(orderEntity.getOrderStatus());
		List<String> errorList = new ArrayList<>();
		
		if(!StringUtils.isEmpty(orderEntity.getFailureMessages())) {
			List<String> tempFailures=  Arrays.asList(orderEntity.getFailureMessages().split(CommonConstants.COMMA_DELIMETER));
			errorList.addAll(tempFailures);
		}
		
		order.setErrors(errorList);
		
		
		return order;
	}
	
	public List<OrderItem> createOrderItemDomainFromEntity(List<OrderItemEntity> orderItems) {
		return orderItems.stream()
				.map(orderItemEntity -> new OrderItem(new OrderItemId(orderItemEntity.getId()), new Product(new ProductId(orderItemEntity.getProductId())),
						orderItemEntity.getQuantity(), new Money(orderItemEntity.getUnitPrice()),
						new Money(orderItemEntity.getTotalPrice())))
				.collect(Collectors.toList());
	}
	
	public StreetAddress createDomainAddressFromOrderEntity(OrderAddressEntity address) {
        return new StreetAddress(address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity());
    }
}
