package com.learning.orderapplicationservice.external;

import java.time.Instant;
import java.util.List;

import com.learning.commondomain.valueobjects.OrderApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RestrauntApprovalResponse {
	private String id;
	private String sagaId;
	private String orderId;
	private String restaurantId;
	private Instant createdAt;
	private OrderApprovalStatus orderApprovalStatus;
	private List<String> failureMessages;
}
