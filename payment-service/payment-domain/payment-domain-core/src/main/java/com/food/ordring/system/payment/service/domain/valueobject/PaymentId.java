package com.food.ordring.system.payment.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

import java.util.UUID;

public class PaymentId extends BaseId<UUID> {

    public PaymentId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }
}
