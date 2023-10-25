package com.learning.orderservice.core.entities;

import java.util.Map;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.ProductId;
import com.learning.commondomain.valueobjects.RestrauntId;

public class Restraunt extends AggregateRoot<RestrauntId>{
    
	//TODO: convert to stock map later
	private final Map<ProductId, Product> productsMap;
	
	private boolean active;

	public Restraunt(RestrauntId id,Map<ProductId, Product> productsMap , boolean active) {
		super(id);
		this.productsMap = productsMap;
		this.active = active;
	}

	public Map<ProductId, Product> getProductsMap() {
		return productsMap;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	
}
