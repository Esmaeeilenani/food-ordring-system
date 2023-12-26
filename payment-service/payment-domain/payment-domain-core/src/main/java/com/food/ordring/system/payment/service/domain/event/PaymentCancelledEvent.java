package com.food.ordring.system.payment.service.domain.event;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;

public class PaymentCancelledEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCancelledEvent> publisher;

    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt,
                                 DomainEventPublisher<PaymentCancelledEvent> publisher) {
        super(payment, createdAt, Collections.emptyList());
        this.publisher = publisher;
    }

    @Override
    public void fire() {
        publisher.publish(this);
    }
}
