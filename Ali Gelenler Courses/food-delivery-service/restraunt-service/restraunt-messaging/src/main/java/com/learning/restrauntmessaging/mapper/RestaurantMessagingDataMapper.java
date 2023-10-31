package com.learning.restrauntmessaging.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestaurantOrderStatus;
import com.learning.kafkamodel.order.OrderApprovalStatus;
import com.learning.kafkamodel.order.RestaurantApprovalRequestAvroModel;
import com.learning.kafkamodel.order.RestaurantApprovalResponseAvroModel;
import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;
import com.learning.restrauntapplicationservice.outbox.model.OrderEventPayload;
import com.learning.restrauntdomaincore.entities.Product;
import com.learning.restrauntdomaincore.events.OrderApprovedEvent;
import com.learning.restrauntdomaincore.events.OrderRejectedEvent;

@Component
public class RestaurantMessagingDataMapper {

	public RestaurantApprovalResponseAvroModel orderApprovedEventToRestaurantApprovalResponseAvroModel(
			OrderApprovedEvent orderApprovedEvent) {
		   return RestaurantApprovalResponseAvroModel.newBuilder()
	                .setId(UUID.randomUUID().toString())
	                .setSagaId("")
	                .setOrderId(orderApprovedEvent.getOrderApproval().getOrderId().getValue().toString())
	                .setRestaurantId(orderApprovedEvent.getRestrauntId().getValue().toString())
	                .setCreatedAt(orderApprovedEvent.getCreatedAt().toInstant())
	                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderApprovedEvent.
	                        getOrderApproval().getOrderApprovalStatus().name()))
	                .setFailureMessages(orderApprovedEvent.getFailureMessages())
	                .build();
	}

	public RestaurantApprovalResponseAvroModel orderRejectedEventToRestaurantApprovalResponseAvroModel(
			OrderRejectedEvent orderRejectedEvent) {
	     return RestaurantApprovalResponseAvroModel.newBuilder()
	                .setId(UUID.randomUUID().toString())
	                .setSagaId("")
	                .setOrderId(orderRejectedEvent.getOrderApproval().getOrderId().getValue().toString())
	                .setRestaurantId(orderRejectedEvent.getRestrauntId().getValue().toString())
	                .setCreatedAt(orderRejectedEvent.getCreatedAt().toInstant())
	                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderRejectedEvent.
	                        getOrderApproval().getOrderApprovalStatus().name()))
	                .setFailureMessages(orderRejectedEvent.getFailureMessages())
	                .build();
	}
	
	public RestrauntApprovalRequest restaurantApprovalRequestAvroModelToRestaurantApproval(
			RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel) {
	        return RestrauntApprovalRequest.builder()
	                .id(restaurantApprovalRequestAvroModel.getId())
	                .sagaId(restaurantApprovalRequestAvroModel.getSagaId())
	                .restaurantId(restaurantApprovalRequestAvroModel.getRestaurantId())
	                .orderId(restaurantApprovalRequestAvroModel.getOrderId())
	                .restaurantOrderStatus(RestaurantOrderStatus.valueOf(restaurantApprovalRequestAvroModel
	                        .getRestaurantOrderStatus().name()))
	                .products(restaurantApprovalRequestAvroModel.getProducts()
	                        .stream().map(avroModel ->
	                        		new Product(new ProductId(UUID.fromString(avroModel.getId())), null, null, avroModel.getQuantity(), false))
	                        .collect(Collectors.toList()))
	                .price(restaurantApprovalRequestAvroModel.getPrice())
	                .createdAt(restaurantApprovalRequestAvroModel.getCreatedAt())
	                .build();
	    }

	public RestaurantApprovalResponseAvroModel orderEventPayloadToRestaurantApprovalResponseAvroModel(String sagaId,
			OrderEventPayload orderEventPayload) {
		 return RestaurantApprovalResponseAvroModel.newBuilder()
	                .setId(UUID.randomUUID().toString())
	                .setSagaId(sagaId)
	                .setOrderId(orderEventPayload.getOrderId())
	                .setRestaurantId(orderEventPayload.getRestaurantId())
	                .setCreatedAt(orderEventPayload.getCreatedAt().toInstant())
	                .setOrderApprovalStatus(OrderApprovalStatus.valueOf(orderEventPayload.getOrderApprovalStatus()))
	                .setFailureMessages(orderEventPayload.getFailureMessages())
	                .build();
	}

}
