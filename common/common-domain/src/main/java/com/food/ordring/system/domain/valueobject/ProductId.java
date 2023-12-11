package com.food.ordring.system.domain.valueobject;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductId extends BaseId<UUID> {

    public ProductId(UUID value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }


}
