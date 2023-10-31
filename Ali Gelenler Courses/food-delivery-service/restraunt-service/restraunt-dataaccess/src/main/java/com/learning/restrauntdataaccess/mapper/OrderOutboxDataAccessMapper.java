package com.learning.restrauntdataaccess.mapper;

import org.springframework.stereotype.Component;

import com.learning.restrauntapplicationservice.outbox.model.OrderOutboxMessage;
import com.learning.restrauntdataaccess.entities.OrderOutboxEntity;

@Component
public class OrderOutboxDataAccessMapper {

    public OrderOutboxEntity orderOutboxMessageToOutboxEntity(OrderOutboxMessage orderOutboxMessage) {
        return OrderOutboxEntity.builder()
                .id(orderOutboxMessage.getId())
                .sagaId(orderOutboxMessage.getSagaId())
                .createdAt(orderOutboxMessage.getCreatedAt())
                .type(orderOutboxMessage.getType())
                .payload(orderOutboxMessage.getPayload())
                .outboxStatus(orderOutboxMessage.getOutboxStatus())
                .approvalStatus(orderOutboxMessage.getApprovalStatus())
                .version(orderOutboxMessage.getVersion())
                .build();
    }

    public OrderOutboxMessage orderOutboxEntityToOrderOutboxMessage(OrderOutboxEntity paymentOutboxEntity) {
        return OrderOutboxMessage.builder()
                .id(paymentOutboxEntity.getId())
                .sagaId(paymentOutboxEntity.getSagaId())
                .createdAt(paymentOutboxEntity.getCreatedAt())
                .type(paymentOutboxEntity.getType())
                .payload(paymentOutboxEntity.getPayload())
                .outboxStatus(paymentOutboxEntity.getOutboxStatus())
                .approvalStatus(paymentOutboxEntity.getApprovalStatus())
                .version(paymentOutboxEntity.getVersion())
                .build();
    }

}
