package com.learning.restrauntdomaincore.entities;

import java.util.List;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.OrderStatus;

public class OrderDetail extends BaseEntity<OrderId> {

	private OrderStatus orderStatus;
	private Money totalAmount;
	private final List<Product> products;

	public OrderDetail(OrderId id, OrderStatus orderStatus, Money totalAmount, List<Product> products) {
		super(id);
		this.orderStatus = orderStatus;
		this.totalAmount = totalAmount;
		this.products = products;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Money getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Money totalAmount) {
		this.totalAmount = totalAmount;
	}

	public List<Product> getProducts() {
		return products;
	}

}
