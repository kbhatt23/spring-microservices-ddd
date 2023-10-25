package com.learning.restrauntapplicationservice.input;

import com.learning.restrauntapplicationservice.dto.RestrauntApprovalRequest;

public interface RestrauntApprovalRequestMessageListener {

	public void approveOrder(RestrauntApprovalRequest restrauntApprovalRequest);
}
