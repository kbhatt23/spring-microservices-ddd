package com.learning.orderservice.core.entities;

import com.learning.commondomain.entities.BaseEntity;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.orderservice.core.valueobjects.OrderItemId;

public class OrderItem extends BaseEntity<OrderItemId> {

	private OrderId orderId;

	private final Product product;

	private final int quantity;

	private final Money unitPrice;

	private final Money totalPrice;

	public OrderItem(OrderItemId id, Product product, int quantity, Money unitPrice, Money totalPrice) {
		super(id);
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
	}
	
	public OrderId getOrderId() {
		return orderId;
	}

	public void setOrderId(OrderId orderId) {
		this.orderId = orderId;
	}

	public Product getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public Money getUnitPrice() {
		return unitPrice;
	}

	public Money getTotalPrice() {
		return totalPrice;
	}

	public boolean isPriceValid() {
		return unitPrice != null && unitPrice.isGreaterThanZero()
				&& totalPrice != null && totalPrice.isGreaterThanZero()
				&& unitPrice.equals(product.getPrice())
				 && unitPrice.multiply(quantity).equals(totalPrice);
	}

	public void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
      this.orderId=orderId;
      super.setId(orderItemId);
	}

}
