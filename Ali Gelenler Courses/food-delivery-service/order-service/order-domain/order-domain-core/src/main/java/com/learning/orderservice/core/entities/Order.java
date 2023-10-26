package com.learning.orderservice.core.entities;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.learning.commondomain.entities.AggregateRoot;
import com.learning.commondomain.valueobjects.CustomerId;
import com.learning.commondomain.valueobjects.Money;
import com.learning.commondomain.valueobjects.OrderId;
import com.learning.commondomain.valueobjects.OrderStatus;
import com.learning.commondomain.valueobjects.RestrauntId;
import com.learning.orderservice.core.exceptions.OrderDomainException;
import com.learning.orderservice.core.valueobjects.OrderItemId;
import com.learning.orderservice.core.valueobjects.StreetAddress;
import com.learning.orderservice.core.valueobjects.TrackingId;

public class Order extends AggregateRoot<OrderId>{

	private final CustomerId customerId;
	
	private final RestrauntId restrauntId;
	
	private final StreetAddress streetAddress;
	
	private final Money price;
	
	private final List<OrderItem> items;
	
	private TrackingId trackingId;
	
	private OrderStatus orderStatus;
	
	private List<String> errors;
	
	public Order(OrderId id, CustomerId customerId, RestrauntId restrauntId, StreetAddress streetAddress, Money price,
			List<OrderItem> items) {
		super(id);
		this.customerId = customerId;
		this.restrauntId = restrauntId;
		this.streetAddress = streetAddress;
		this.price = price;
		this.items = items;
	}

	public void initializeOrder() {
        setId(new OrderId(UUID.randomUUID()));
        trackingId = new TrackingId(UUID.randomUUID());
        orderStatus = OrderStatus.PENDING;
        initializeOrderItems();
    }
	
	private void initializeOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem: items) {
            orderItem.initializeOrderItem(super.getId(), new OrderItemId(itemId++));
        }
    }
	
	public void validateOrder() {
		validateInitialOrder();
		validateTotalPrice();
		validateItemsPrice();
	}
	
	private void validateInitialOrder() {
		if(getId() != null || orderStatus != null) {
			 throw new OrderDomainException("Order is not in correct state for initialization!");
		}
	}
	
	private void validateTotalPrice() {
		if(price == null || !price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero!");
		}
	}
	
	private void validateItemsPrice() {
		Money itemsTotalPrice = items.stream()
		     .map(item -> {
		    	 validateItemPrice(item);
		    	 return item.getTotalPrice();
		     }).reduce(Money.ZERO_MONEY, Money :: add);
		
		if(!price.equals(itemsTotalPrice))
			 throw new OrderDomainException("Total price: " + price.getAmount()
             + " is not equal to Order items total: " + itemsTotalPrice.getAmount() + "!");
	}

	private void validateItemPrice(OrderItem orderItem) {
		  if (!orderItem.isPriceValid()) {
	            throw new OrderDomainException("Order item price: " + orderItem.getUnitPrice().getAmount() +
	                    " is not valid for product " + orderItem.getProduct().getId().getValue());
	        }
	}
	
	public void pay() {
		if(!OrderStatus.PENDING.equals(orderStatus))
			throw new OrderDomainException("Order is not in correct state for pay operation!");

		orderStatus = OrderStatus.PAID;
	}

	public void approve() {
		if(!OrderStatus.PAID.equals(orderStatus))
			throw new OrderDomainException("Order is not in correct state for approve operation!");

		orderStatus = OrderStatus.APPROVED;
	}
	
	public void initCancelling(List<String> failureMessages) {
		if(!OrderStatus.PAID.equals(orderStatus))
			throw new OrderDomainException("Order is not in correct state for initCancelling operation!");

		orderStatus = OrderStatus.CANCELLING;
		updateFailureMessages(failureMessages);
	}
	
	public void cancel(List<String> failureMessages) {
		//cancelling state is done only by restraunt cancel
		//for payment cancel it will be still in pending state
		if(!(OrderStatus.CANCELLING.equals(orderStatus) || OrderStatus.PENDING.equals(orderStatus)))
			throw new OrderDomainException("Order is not in correct state for cancel operation!");

		orderStatus = OrderStatus.CANCELLED;
		updateFailureMessages(failureMessages);
	}

	
	private void updateFailureMessages(List<String> failureMessages) {
		if(this.errors != null && failureMessages != null) {
			this.errors.addAll(failureMessages);
		}
		
		if(this.errors == null)
			this.errors = failureMessages.stream().filter(item -> !item.isBlank()).collect(Collectors.toList());
	}

	public CustomerId getCustomerId() {
		return customerId;
	}

	public RestrauntId getRestrauntId() {
		return restrauntId;
	}

	public StreetAddress getStreetAddress() {
		return streetAddress;
	}

	public Money getPrice() {
		return price;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public TrackingId getTrackingId() {
		return trackingId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setTrackingId(TrackingId trackingId) {
		this.trackingId = trackingId;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

}
