package com.learning.commondataaccess.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_restraunt_m_view", schema = "restraunt")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(RestrauntEntityId.class)
public class RestrauntEntity {

	@Id
	private UUID restrauntId;
	
	@Id
	private UUID productId;
	
	private String restrauntName;
	
	private String productName;
	
	private BigDecimal productPrice;
	
	@Column(name = "restraunt_active")
	private Boolean active;
	
	@Column(name = "product_available")
	private Boolean productInStock;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((restrauntId == null) ? 0 : restrauntId.hashCode());
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
		RestrauntEntity other = (RestrauntEntity) obj;
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (restrauntId == null) {
			if (other.restrauntId != null)
				return false;
		} else if (!restrauntId.equals(other.restrauntId))
			return false;
		return true;
	}
	
	
}
