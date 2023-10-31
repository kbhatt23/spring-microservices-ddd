package com.learning.restrauntapplicationservice.mapper;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;
import com.learning.restrauntapplicationservice.outbox.model.OrderEventPayload;
import com.learning.restrauntdomaincore.entities.OrderDetail;
import com.learning.restrauntdomaincore.entities.Product;
import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;

@Component
public class RestrauntMapper {
	public Restraunt restaurantApprovalRequestToRestaurant(RestrauntApprovalRequest restrauntApprovalRequest) {
		return new Restraunt(new RestrauntId(UUID.fromString(restrauntApprovalRequest.getRestaurantId())), null, false,
				orderDetailDomainFromRequest(restrauntApprovalRequest));

	}

	private OrderDetail orderDetailDomainFromRequest(RestrauntApprovalRequest restrauntApprovalRequest) {
		return new OrderDetail(new OrderId(UUID.fromString(restrauntApprovalRequest.getOrderId())),
				OrderStatus.valueOf(restrauntApprovalRequest.getRestaurantOrderStatus().name()),
				new Money(restrauntApprovalRequest.getPrice()),
				restrauntApprovalRequest.getProducts().stream()
						.map(productReq -> new Product(productReq.getId(), null, null, productReq.getQuantity(), false))
						.collect(Collectors.toList()));
	}

	public OrderEventPayload orderApprovalEventToOrderEventPayload(OrderApprovalEvent orderApprovalEvent) {
		return OrderEventPayload.builder()
				.orderId(orderApprovalEvent.getOrderApproval().getOrderId().getValue().toString())
				.restaurantId(orderApprovalEvent.getRestrauntId().getValue().toString())
				.orderApprovalStatus(orderApprovalEvent.getOrderApproval().getOrderApprovalStatus().name())
				.createdAt(orderApprovalEvent.getCreatedAt()).failureMessages(orderApprovalEvent.getFailureMessages())
				.build();
	}

}