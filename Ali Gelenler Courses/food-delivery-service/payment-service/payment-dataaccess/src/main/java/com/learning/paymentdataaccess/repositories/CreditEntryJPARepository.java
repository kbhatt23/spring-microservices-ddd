package com.learning.paymentdataaccess.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.paymentdataaccess.entities.CreditEntryEntity;

@Repository
public interface CreditEntryJPARepository extends JpaRepository<CreditEntryEntity, UUID> {

	Optional<CreditEntryEntity> findByCustomerId(UUID customerId);
}
