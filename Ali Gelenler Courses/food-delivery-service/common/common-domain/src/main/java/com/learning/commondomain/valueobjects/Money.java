package com.learning.commondomain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;

//why to use Money and not BigDecimal directly on user class
//add context and hence adds readability
//logic can also be added + if field contains more than one state holder can also be added here
//should be immuable
public class Money {
	
	private final BigDecimal amount;
	
	public static final Money ZERO_MONEY = new Money(BigDecimal.ZERO);

	public Money(BigDecimal amount) {
		if(amount == null)
			throw new IllegalArgumentException("amount can not be null in money");
		this.amount = amount;
	}
	
	public boolean isGreaterThanZero() {
		return (this.amount != null) && (this.amount.compareTo(BigDecimal.ZERO) > 0) ;
	}
	
	public boolean isGreaterThan(Money money) {
		return (this.amount != null) && (money != null) && (this.amount.compareTo(money.amount) > 0) ;
	}
	
	//immutable methods
	public Money add(Money money) {
		if(money == null)
			throw new IllegalArgumentException("empty money can not be added");
		
		return new Money(scale(this.amount.add(money.amount)));
	}
	
	public Money substract(Money money) {
		if(money == null)
			throw new IllegalArgumentException("empty money can not be substracted");
		
		return new Money(scale(this.amount.subtract(money.amount)));
	}
	
	public Money multiply(int multiplier) {
		return new Money(scale(this.amount.multiply(new BigDecimal(multiplier))));
	}
	
	private BigDecimal scale(BigDecimal input) {
		return input.setScale(2, RoundingMode.HALF_EVEN);
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
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
		Money other = (Money) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		return true;
	}
	
	
	

}
