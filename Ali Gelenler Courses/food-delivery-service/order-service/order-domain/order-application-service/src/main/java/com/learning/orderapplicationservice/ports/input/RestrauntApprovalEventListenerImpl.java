package com.learning.orderapplicationservice.ports.input;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.learning.orderapplicationservice.external.RestrauntApprovalResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Validated
@Slf4j
public class RestrauntApprovalEventListenerImpl implements RestrauntApprovalEventListener {

	@Override
	public void orderApproved(RestrauntApprovalResponse restrauntApprovalResponse) {

	}

	@Override
	public void orderRejected(RestrauntApprovalResponse restrauntApprovalResponse) {

	}

}
