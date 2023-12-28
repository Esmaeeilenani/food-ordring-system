package com.food.ordring.system.payment.service.dataaccess.credithistory.repository;

import com.food.ordring.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditHistoryJpaRepository extends JpaRepository<CreditHistoryEntity, UUID> {

    List<CreditHistoryEntity>findAllByCustomerId(UUID customerId);


}
