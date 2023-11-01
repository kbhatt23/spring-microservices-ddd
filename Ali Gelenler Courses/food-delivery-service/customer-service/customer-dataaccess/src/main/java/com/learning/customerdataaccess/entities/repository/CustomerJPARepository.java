package com.learning.customerdataaccess.entities.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.customerdataaccess.entities.CustomerEntity;

@Repository
public interface CustomerJPARepository extends JpaRepository<CustomerEntity, UUID> {

}
