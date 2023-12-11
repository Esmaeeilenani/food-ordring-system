package com.food.ordring.system.order.service.domain.valueobject;

import com.food.ordring.system.domain.valueobject.BaseId;

public class OrderItemId extends BaseId <Long> {


    public OrderItemId(Long value) {
        super(value);
    }

    @Override
    public String toString() {
        return super.getValue().toString();
    }


}
