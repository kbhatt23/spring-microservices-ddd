package com.learning.dataaccess.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.learning.commondomain.valueobjects.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

	@Id
	private UUID id;

	private UUID customerId;

	private UUID restrauntId;

	private UUID trackingId;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
	private OrderAddressEntity orderAddressEntity;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderItemEntity> items;
	
    private String failureMessages;
    
    private BigDecimal price;

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
		OrderEntity other = (OrderEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
}
