package com.food.ordring.system.order.service.domain.event;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {


    public OrderCreatedEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);

    }

}
