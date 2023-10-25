package com.learning.paymentdataaccess.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.paymentdataaccess.entities.PaymentEntity;

@Repository
public interface PaymentJPARepository extends JpaRepository<PaymentEntity, UUID> {

	public Optional<PaymentEntity> findByOrderId(UUID orderId);
}
