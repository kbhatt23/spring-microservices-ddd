package com.learning.paymentdataaccess.entities;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.learning.commondomain.valueobjects.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

	@Id
	private UUID id;
	
	private UUID customerId;
	
	private UUID orderId;
	
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	private PaymentStatus status;
	
	private ZonedDateTime createdAt;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentEntity other = (PaymentEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
