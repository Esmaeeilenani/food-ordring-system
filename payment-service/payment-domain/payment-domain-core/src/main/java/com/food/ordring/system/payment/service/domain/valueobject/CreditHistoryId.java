package com.food.ordring.system.payment.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditHistoryId extends BaseId<UUID> {

    public CreditHistoryId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
