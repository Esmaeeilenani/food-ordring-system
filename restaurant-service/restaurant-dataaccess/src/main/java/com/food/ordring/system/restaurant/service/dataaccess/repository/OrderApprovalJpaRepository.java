package com.food.ordring.system.restaurant.service.dataaccess.repository;

import com.food.ordring.system.restaurant.service.dataaccess.entity.OrderApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {


}
