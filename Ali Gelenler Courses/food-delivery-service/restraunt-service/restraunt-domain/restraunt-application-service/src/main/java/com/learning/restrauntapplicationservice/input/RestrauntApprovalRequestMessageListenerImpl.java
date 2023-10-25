package com.learning.restrauntapplicationservice.input;

import org.springframework.stereotype.Service;

import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;
import com.learning.restrauntapplicationservice.helper.RestrauntApprovalRequestHelper;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RestrauntApprovalRequestMessageListenerImpl implements RestrauntApprovalRequestMessageListener {

	private final RestrauntApprovalRequestHelper restrauntApprovalRequestHelper;

	public RestrauntApprovalRequestMessageListenerImpl(RestrauntApprovalRequestHelper restrauntApprovalRequestHelper) {
		this.restrauntApprovalRequestHelper = restrauntApprovalRequestHelper;
	}

	@Override
	public void approveOrder(RestrauntApprovalRequest restrauntApprovalRequest) {
		OrderApprovalEvent persistOrderApproval = restrauntApprovalRequestHelper
				.persistOrderApproval(restrauntApprovalRequest);

		persistOrderApproval.fire();
	}

}
