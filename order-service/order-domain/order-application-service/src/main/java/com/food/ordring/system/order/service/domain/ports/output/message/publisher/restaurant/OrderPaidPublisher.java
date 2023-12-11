package com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;

public interface OrderPaidPublisher extends DomainEventPublisher<OrderPaidEvent> {
}
