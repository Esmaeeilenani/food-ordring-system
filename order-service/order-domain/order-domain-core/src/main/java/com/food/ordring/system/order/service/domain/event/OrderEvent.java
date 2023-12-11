package com.food.ordring.system.order.service.domain.event;

import com.food.ordring.system.domain.event.DomainEvent;
import com.food.ordring.system.order.service.domain.entity.Order;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public abstract class OrderEvent implements DomainEvent<Order> {

    protected final Order order;
    protected final ZonedDateTime createdAt;

    protected OrderEvent(Order order, ZonedDateTime createdAt) {
        this.order = order;
        this.createdAt = createdAt;
    }
}
