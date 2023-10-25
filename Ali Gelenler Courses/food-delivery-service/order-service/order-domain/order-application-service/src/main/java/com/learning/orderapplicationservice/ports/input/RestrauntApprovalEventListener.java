package com.learning.orderapplicationservice.ports.input;

import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;

public interface RestrauntApprovalEventListener {

	void orderApproved(RestrauntApprovalResponse restrauntApprovalResponse);
	
	void orderRejected(RestrauntApprovalResponse restrauntApprovalResponse);
}
