package com.food.ordring.system.payment.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

import java.util.UUID;

public class CreditEntryId extends BaseId<UUID> {
    public CreditEntryId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return getValue().toString();
    }
}
