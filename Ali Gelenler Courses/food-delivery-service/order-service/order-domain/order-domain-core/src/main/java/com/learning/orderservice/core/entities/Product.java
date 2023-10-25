package com.learning.orderservice.core.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.ProductId;

public class Product extends BaseEntity<ProductId>{

	private String name;
	
	private Money price;

	public Product(ProductId productId ,String name, Money price) {
		super(productId);
		this.name = name;
		this.price = price;
	}
	
	public Product(ProductId id) {
		super(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Money getPrice() {
		return price;
	}

	public void setPrice(Money price) {
		this.price = price;
	}
	
	public void updateProductInfo(Product other) {
		this.name=other.name;
		this.price=other.price;
	}
	
	
}
