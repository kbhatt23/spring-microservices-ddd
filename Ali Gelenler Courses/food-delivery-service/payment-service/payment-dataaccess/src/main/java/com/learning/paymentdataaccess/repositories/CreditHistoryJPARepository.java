package com.learning.paymentdataaccess.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.learning.paymentdataaccess.entities.CreditHistoryEntity;

@Repository
public interface CreditHistoryJPARepository extends JpaRepository<CreditHistoryEntity, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<List<CreditHistoryEntity>> findByCustomerId(UUID customerId);
}
