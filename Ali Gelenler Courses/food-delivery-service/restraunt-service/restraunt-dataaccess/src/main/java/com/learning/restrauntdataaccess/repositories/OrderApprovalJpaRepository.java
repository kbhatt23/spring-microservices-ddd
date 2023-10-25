package com.learning.restrauntdataaccess.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.restrauntdataaccess.entities.OrderApprovalEntity;

@Repository
public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID>{

}
