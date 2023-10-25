package com.learning.orderapplicationservice.ports.input;

import javax.validation.Valid;

import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.queries.TrackOrderQuery;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderapplicationservice.response.TrackOrderResponse;

public interface OrderApplicationService {

	CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);
	
	TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);
}
