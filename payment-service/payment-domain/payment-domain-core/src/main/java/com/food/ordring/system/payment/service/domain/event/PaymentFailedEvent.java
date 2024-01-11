package com.food.ordring.system.payment.service.domain.event;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;
@Getter
public class PaymentFailedEvent extends PaymentEvent {

    private final DomainEventPublisher<PaymentFailedEvent> publisher;

    public PaymentFailedEvent(Payment payment, ZonedDateTime createdAt, List<String> failureMessages, DomainEventPublisher<PaymentFailedEvent> publisher) {
        super(payment, createdAt, failureMessages);
        this.publisher = publisher;
    }


    public void fire() {
        publisher.publish(this);
    }
}
