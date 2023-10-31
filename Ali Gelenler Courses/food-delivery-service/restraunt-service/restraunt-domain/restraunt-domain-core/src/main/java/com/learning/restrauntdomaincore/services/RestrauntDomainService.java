package com.learning.restrauntdomaincore.services;

import java.util.List;

import com.learning.restrauntdomaincore.entities.Restraunt;
import com.learning.restrauntdomaincore.events.OrderApprovalEvent;

public interface RestrauntDomainService {

	public OrderApprovalEvent validateOrder(Restraunt restraunt, List<String> failureMessages);
}
