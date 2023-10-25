package com.learning.restrauntdomaincore.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.ProductId;

public class Product extends BaseEntity<ProductId> {
	private String name;
	private Money price;
	private final int quantity;
	private boolean available;

	public Product(ProductId id, String name, Money price, int quantity, boolean available) {
		super(id);
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.available = available;
	}

	public void updateWithConfirmedNamePriceAndAvailability(String name, Money price, boolean available) {
		this.name = name;
		this.price = price;
		this.available = available;
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

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getQuantity() {
		return quantity;
	}

}
