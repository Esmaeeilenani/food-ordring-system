package com.food.ordring.system.payment.service.domain.event;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.Collections;
@Getter
public class PaymentCompletedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentCompletedEvent> publisher;

    public PaymentCompletedEvent(Payment payment, ZonedDateTime createdAt, DomainEventPublisher<PaymentCompletedEvent> publisher) {
        super(payment, createdAt, Collections.emptyList());
        this.publisher = publisher;
    }


    public void fire() {
        publisher.publish(this);
    }
}
