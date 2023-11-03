package com.learning.restrauntdataaccess.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.learning.commondomain.valueobjects.OrderApprovalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_approval", schema = "restraunt")
@Entity
public class OrderApprovalEntity {
	@Id
	private UUID id;
	
	private UUID restrauntId;
	
	private UUID orderId;
	
	@Enumerated(EnumType.STRING)
	private OrderApprovalStatus status;
}
