package com.food.ordring.system.payment.service.dataaccess.credithistory.adapter;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.ordring.system.payment.service.dataaccess.credithistory.mapper.CreditHistoryDataAccessMapper;
import com.food.ordring.system.payment.service.dataaccess.credithistory.repository.CreditHistoryJpaRepository;
import com.food.ordring.system.payment.service.domain.entity.CreditHistory;
import com.food.ordring.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class CreditHistoryRepositoryImpl implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;
    private final CreditHistoryDataAccessMapper creditHistoryDataAccessMapper;

    public CreditHistoryRepositoryImpl(CreditHistoryJpaRepository creditHistoryJpaRepository,
                                       CreditHistoryDataAccessMapper creditHistoryDataAccessMapper) {
        this.creditHistoryJpaRepository = creditHistoryJpaRepository;
        this.creditHistoryDataAccessMapper = creditHistoryDataAccessMapper;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        return creditHistoryDataAccessMapper.creditHistoryEntityToCreditHistory(creditHistoryJpaRepository
                .save(creditHistoryDataAccessMapper.creditHistoryToCreditHistoryEntity(creditHistory)));
    }

    @Override
    public List<CreditHistory> findAllByCustomerId(UUID customerId) {
        List<CreditHistoryEntity> historyEntities = creditHistoryJpaRepository.findAllByCustomerId(customerId);
        return historyEntities
                .stream()
                .map(creditHistoryDataAccessMapper::creditHistoryEntityToCreditHistory)
                .toList();
    }
}
