package com.learning.orderapplicationservice.ports.input;

import com.learning.orderapplicationservice.oubox.model.CustomerModel;

public interface CustomerMessageListener {

	public void customerCreated(CustomerModel customerModel);
}