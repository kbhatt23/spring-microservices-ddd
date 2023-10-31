package com.learning.restrauntapplicationservice.outbox.model;

import java.time.ZonedDateTime;
import java.util.UUID;

import com.learning.commondomain.valueobjects.OrderApprovalStatus;
import com.learning.outbox.OutboxStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderOutboxMessage {
    private UUID id;
    private UUID sagaId;
    private ZonedDateTime createdAt;
    private ZonedDateTime processedAt;
    private String type;
    private String payload;
    private OutboxStatus outboxStatus;
    private OrderApprovalStatus approvalStatus;
    private int version;

    public void setOutboxStatus(OutboxStatus status) {
        this.outboxStatus = status;
    }
}
