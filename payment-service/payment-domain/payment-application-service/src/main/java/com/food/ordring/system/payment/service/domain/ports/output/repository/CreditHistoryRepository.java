package com.food.ordring.system.payment.service.domain.ports.output.repository;

import com.food.ordring.system.payment.service.domain.entity.CreditHistory;

import java.util.List;
import java.util.UUID;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);

    List<CreditHistory> findAllByCustomerId(UUID customerId);
}
