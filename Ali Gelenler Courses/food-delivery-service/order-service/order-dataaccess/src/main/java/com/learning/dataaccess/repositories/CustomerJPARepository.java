package com.learning.dataaccess.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.learning.dataaccess.entity.CustomerEntity;

@Repository
public interface CustomerJPARepository extends JpaRepository<CustomerEntity, UUID> {

}
