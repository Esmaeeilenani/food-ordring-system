package com.food.ordring.system.order.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

import java.util.UUID;

public class TrackingId extends BaseId<UUID> {

    public TrackingId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }
}
