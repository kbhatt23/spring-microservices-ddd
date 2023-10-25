package com.learning.commondataaccess.entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestrauntJPARepository extends JpaRepository<RestrauntEntity, RestrauntEntityId> {

	public Optional<List<RestrauntEntity>> findByRestrauntIdAndProductIdIn(UUID restrauntId, List<UUID> productIds);
}
