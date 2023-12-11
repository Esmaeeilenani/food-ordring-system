package com.food.ordring.system.domain.valueobject;

import lombok.Getter;

import java.util.UUID;
@Getter
public class OrderId extends BaseId<UUID>{


    public OrderId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }
}
