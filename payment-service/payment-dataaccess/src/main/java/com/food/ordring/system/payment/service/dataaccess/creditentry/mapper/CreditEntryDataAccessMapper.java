package com.food.ordring.system.payment.service.dataaccess.creditentry.mapper;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.ordring.system.payment.service.domain.entity.CreditEntry;
import org.springframework.stereotype.Component;

@Component
public class CreditEntryDataAccessMapper {

    public CreditEntry creditEntryEntityToCreditEntry(CreditEntryEntity creditEntryEntity) {
        return CreditEntry.builder()
                .id(creditEntryEntity.getId())
                .customerId(new CustomerId(creditEntryEntity.getCustomerId()))
                .totalCreditAmount(new Money(creditEntryEntity.getTotalCreditAmount()))
                .build();
    }

    public CreditEntryEntity creditEntryToCreditEntryEntity(CreditEntry creditEntry) {
        return CreditEntryEntity.builder()
                .id(creditEntry.getId().getValue())
                .customerId(creditEntry.getCustomerId().getValue())
                .totalCreditAmount(creditEntry.getTotalCreditAmount().getAmount())
                .build();
    }

}
