package com.learning.restrauntapplicationservice.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;
import com.learning.restrauntapplicationservice.mapper.RestrauntMapper;
import com.learning.restrauntapplicationservice.output.OrderApprovalRepository;
import com.learning.restrauntapplicationservice.output.OrderApprovedMessagePublisher;
import com.learning.restrauntapplicationservice.output.OrderRejectedMessagePublisher;
import com.learning.restrauntapplicationservice.output.RestrauntRepository;
import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;
import com.learning.restrauntdomaincore.exceptions.RestrauntNotFoundException;
import com.learning.restrauntdomaincore.services.RestrauntDomainService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RestrauntApprovalRequestHelper {

	private final RestrauntDomainService restrauntDomainService;

	private final RestrauntMapper restrauntMapper;

	private final OrderApprovedMessagePublisher orderApprovedMessagePublisher;

	private final OrderRejectedMessagePublisher orderRejectedMessagePublisher;

	private final RestrauntRepository restrauntRepository;

	private final OrderApprovalRepository orderApprovalRepository;

	public RestrauntApprovalRequestHelper(RestrauntDomainService restrauntDomainService,
			RestrauntMapper restrauntMapper, OrderApprovedMessagePublisher orderApprovedMessagePublisher,
			OrderRejectedMessagePublisher orderRejectedMessagePublisher, RestrauntRepository restrauntRepository,
			OrderApprovalRepository orderApprovalRepository) {
		this.restrauntDomainService = restrauntDomainService;
		this.restrauntMapper = restrauntMapper;
		this.orderApprovedMessagePublisher = orderApprovedMessagePublisher;
		this.orderRejectedMessagePublisher = orderRejectedMessagePublisher;
		this.restrauntRepository = restrauntRepository;
		this.orderApprovalRepository = orderApprovalRepository;
	}

	@Transactional
	public OrderApprovalEvent persistOrderApproval(RestrauntApprovalRequest restrauntApprovalRequest) {
		log.info("Processing restaurant approval for order id: {}", restrauntApprovalRequest.getOrderId());
		Restraunt restraunt = findRestraunt(restrauntApprovalRequest);

		List<String> failureMessages = new ArrayList<>();

		OrderApprovalEvent orderApprovalEvent = restrauntDomainService.validateOrder(restraunt, failureMessages,
				orderApprovedMessagePublisher, orderRejectedMessagePublisher);

		orderApprovalRepository.save(restraunt.getOrderApproval());

		return orderApprovalEvent;
	}

	private Restraunt findRestraunt(RestrauntApprovalRequest restrauntApprovalRequest) {

		Restraunt restraunt = restrauntMapper.restaurantApprovalRequestToRestaurant(restrauntApprovalRequest);

		Restraunt foundRestraunt = restrauntRepository.findRestaurantInformation(restraunt).orElseThrow(() -> {
			log.error("Restaurant with id " + restraunt.getId() + " not found!");
			throw new RestrauntNotFoundException("Restaurant with id " + restraunt.getId().getValue() + " not found!");
		});

		restraunt.setActive(foundRestraunt.isActive());
		restraunt.getOrderDetail().getProducts()
				.forEach(product -> foundRestraunt.getOrderDetail().getProducts().forEach(p -> {
					if (p.getId().equals(product.getId())) {
						product.updateWithConfirmedNamePriceAndAvailability(p.getName(), p.getPrice(), p.isAvailable());
					}
				}));
		restraunt.getOrderDetail().setId(new OrderId(UUID.fromString(restrauntApprovalRequest.getOrderId())));

		return restraunt;
	}

}
