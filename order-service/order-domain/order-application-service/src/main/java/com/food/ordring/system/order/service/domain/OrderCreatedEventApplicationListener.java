package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventApplicationListener {

    public final OrderCreatedPublisher orderCreatedPublisher;

    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {
        orderCreatedPublisher.publish(orderCreatedEvent);
    }
}
