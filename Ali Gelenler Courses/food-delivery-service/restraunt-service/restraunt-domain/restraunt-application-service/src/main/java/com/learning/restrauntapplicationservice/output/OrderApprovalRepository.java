package com.learning.restrauntapplicationservice.output;

import com.learning.restrauntdomaincore.entities.OrderApproval;

public interface OrderApprovalRepository {

	OrderApproval save(OrderApproval orderApproval);
}
