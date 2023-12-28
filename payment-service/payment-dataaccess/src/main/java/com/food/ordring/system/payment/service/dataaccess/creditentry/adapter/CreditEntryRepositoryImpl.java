package com.food.ordring.system.payment.service.dataaccess.creditentry.adapter;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.ordring.system.payment.service.dataaccess.creditentry.mapper.CreditEntryDataAccessMapper;
import com.food.ordring.system.payment.service.dataaccess.creditentry.repository.CreditEntryJpaRepository;
import com.food.ordring.system.payment.service.domain.entity.CreditEntry;
import com.food.ordring.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CreditEntryRepositoryImpl implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;
    private final CreditEntryDataAccessMapper creditEntryDataAccessMapper;

    public CreditEntryRepositoryImpl(CreditEntryJpaRepository creditEntryJpaRepository,
                                     CreditEntryDataAccessMapper creditEntryDataAccessMapper) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
        this.creditEntryDataAccessMapper = creditEntryDataAccessMapper;
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        CreditEntryEntity creditEntryEntity = creditEntryDataAccessMapper.creditEntryToCreditEntryEntity(creditEntry);

        creditEntryJpaRepository
                .save(creditEntryEntity);

        return creditEntryDataAccessMapper
                .creditEntryEntityToCreditEntry(creditEntryEntity);
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(UUID customerId) {
        return creditEntryJpaRepository
                .findByCustomerId(customerId)
                .map(creditEntryDataAccessMapper::creditEntryEntityToCreditEntry);
    }
}
