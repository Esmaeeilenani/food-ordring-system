package com.food.ordring.system.payment.service.dataaccess.credithistory.mapper;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import com.food.ordring.system.payment.service.domain.entity.CreditHistory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreditHistoryDataAccessMapper {

    public CreditHistory creditHistoryEntityToCreditHistory(CreditHistoryEntity creditHistoryEntity) {
        return CreditHistory.builder()
                .id(creditHistoryEntity.getId())
                .customerId(new CustomerId(creditHistoryEntity.getCustomerId()))
                .amount(new Money(creditHistoryEntity.getAmount()))
                .transactionType(creditHistoryEntity.getType())
                .build();
    }

    public List<CreditHistory> creditHistoriesEntityToCreditHistories(List<CreditHistoryEntity> entities) {
        List<CreditHistory> creditHistories = new ArrayList<>();
        entities.forEach(creditHistoryEntity -> creditHistories.add(creditHistoryEntityToCreditHistory(creditHistoryEntity)));
        return creditHistories;
    }


    public CreditHistoryEntity creditHistoryToCreditHistoryEntity(CreditHistory creditHistory) {
        return CreditHistoryEntity.builder()
                .id(creditHistory.getId().getValue())
                .customerId(creditHistory.getCustomerId().getValue())
                .amount(creditHistory.getAmount().getAmount())
                .type(creditHistory.getTransactionType())
                .build();
    }

}
