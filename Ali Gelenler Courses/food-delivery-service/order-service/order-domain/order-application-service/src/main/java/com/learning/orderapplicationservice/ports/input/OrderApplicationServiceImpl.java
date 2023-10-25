package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.orderapplicationservice.commands.CreateOrderCommand;
import com.learning.orderapplicationservice.ports.input.handlers.CreateOrderCommandHandler;
import com.learning.orderapplicationservice.ports.input.handlers.TrackOrderQueryHandler;
import com.learning.orderapplicationservice.queries.TrackOrderQuery;
import com.learning.orderapplicationservice.response.CreateOrderResponse;
import com.learning.orderapplicationservice.response.TrackOrderResponse;

//class is not visible outside package as we expose interface exposed to external client
@Service
@Validated
/* public */ class OrderApplicationServiceImpl implements OrderApplicationService {

	private final CreateOrderCommandHandler createOrderCommandHandler;
	
	private final TrackOrderQueryHandler trackOrderQueryHandler;
	
	public OrderApplicationServiceImpl(CreateOrderCommandHandler createOrderCommandHandler,
			TrackOrderQueryHandler trackOrderQueryHandler) {
		this.createOrderCommandHandler = createOrderCommandHandler;
		this.trackOrderQueryHandler = trackOrderQueryHandler;
	}

	@Override
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {

		return createOrderCommandHandler.createOrder(createOrderCommand);
	}

	@Override
	public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
		
		return trackOrderQueryHandler.trackOrder(trackOrderQuery);
	}

}
