package com.food.ordring.system.order.service.domain.event;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.order.service.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderCreatedEvent extends OrderEvent {

    private final DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher;

    public OrderCreatedEvent(Order order, ZonedDateTime createdAt, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventPublisher) {
        super(order, createdAt);
        this.orderCreatedEventPublisher = orderCreatedEventPublisher;
    }

    @Override
    public void fire() {
        orderCreatedEventPublisher.publish(this);
    }
}
