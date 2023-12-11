package com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;

public interface OrderCancelledPublisher extends DomainEventPublisher<OrderCancelledEvent> {
}
