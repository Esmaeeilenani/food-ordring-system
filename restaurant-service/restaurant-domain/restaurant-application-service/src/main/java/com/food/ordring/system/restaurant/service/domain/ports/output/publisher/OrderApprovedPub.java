package com.food.ordring.system.restaurant.service.domain.ports.output.publisher;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.restaurant.service.domain.event.OrderApprovedEvent;

public interface OrderApprovedPub extends DomainEventPublisher<OrderApprovedEvent> {
}
