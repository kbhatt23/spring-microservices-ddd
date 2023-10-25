package com.learning.dataaccess.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dataaccess.entity.OrderEntity;

@Repository
public interface OrderJPARepository extends JpaRepository<OrderEntity, UUID> {

	public Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
