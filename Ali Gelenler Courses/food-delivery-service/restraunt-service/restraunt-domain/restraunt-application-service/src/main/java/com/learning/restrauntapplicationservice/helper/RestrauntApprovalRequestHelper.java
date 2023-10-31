package com.learning.restrauntapplicationservice.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.learning.commondomain.valueobjects.OrderId;
import com.learning.outbox.OutboxStatus;
import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;
import com.learning.restrauntapplicationservice.mapper.RestrauntMapper;
import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntapplicationservice.outbox.scheduler.OrderOutboxHelper;
import com.learning.restrauntapplicationservice.output.OrderApprovalRepository;
import com.learning.restrauntapplicationservice.output.RestrauntApprovalResponseMessagePublisher;
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

	private final RestrauntRepository restrauntRepository;

	private final OrderApprovalRepository orderApprovalRepository;

	private final OrderOutboxHelper orderOutboxHelper;

	private final RestrauntApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher;

	public RestrauntApprovalRequestHelper(RestrauntDomainService restrauntDomainService,
			RestrauntMapper restrauntMapper, RestrauntRepository restrauntRepository,
			OrderApprovalRepository orderApprovalRepository, OrderOutboxHelper orderOutboxHelper,
			RestrauntApprovalResponseMessagePublisher restaurantApprovalResponseMessagePublisher) {
		this.restrauntDomainService = restrauntDomainService;
		this.restrauntMapper = restrauntMapper;
		this.restrauntRepository = restrauntRepository;
		this.orderApprovalRepository = orderApprovalRepository;
		this.orderOutboxHelper = orderOutboxHelper;
		this.restaurantApprovalResponseMessagePublisher = restaurantApprovalResponseMessagePublisher;
	}

	@Transactional
	public void persistOrderApproval(RestrauntApprovalRequest restrauntApprovalRequest) {
		if (publishIfOutboxMessageProcessed(restrauntApprovalRequest)) {
			log.info("An outbox message with saga id: {} already saved to database!",
					restrauntApprovalRequest.getSagaId());
			return;
		}

		log.info("Processing restaurant approval for order id: {}", restrauntApprovalRequest.getOrderId());
		Restraunt restraunt = findRestraunt(restrauntApprovalRequest);

		List<String> failureMessages = new ArrayList<>();

		OrderApprovalEvent orderApprovalEvent = restrauntDomainService.validateOrder(restraunt, failureMessages);

		orderApprovalRepository.save(restraunt.getOrderApproval());

		orderOutboxHelper.saveOrderOutboxMessage(
				restrauntMapper.orderApprovalEventToOrderEventPayload(orderApprovalEvent),
				orderApprovalEvent.getOrderApproval().getOrderApprovalStatus(), OutboxStatus.STARTED,
				UUID.fromString(restrauntApprovalRequest.getSagaId()));

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

	private boolean publishIfOutboxMessageProcessed(RestrauntApprovalRequest restaurantApprovalRequest) {
		Optional<OrderOutboxMessage> orderOutboxMessage = orderOutboxHelper
				.getCompletedOrderOutboxMessageBySagaIdAndOutboxStatus(
						UUID.fromString(restaurantApprovalRequest.getSagaId()), OutboxStatus.COMPLETED);
		if (orderOutboxMessage.isPresent()) {
			restaurantApprovalResponseMessagePublisher.publish(orderOutboxMessage.get(),
					orderOutboxHelper::updateOutboxStatus);
			return true;
		}
		return false;
	}
}