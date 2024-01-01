package com.food.ordring.system.restaurant.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

import java.util.UUID;

public class OrderApprovalId extends BaseId<UUID> {
    public OrderApprovalId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }
}
