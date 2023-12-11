package com.food.ordring.system.domain.valueobject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CustomerId extends BaseId<UUID>{

    public CustomerId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }
}
