package com.food.ordring.system.payment.service.domain.ports.output.publisher;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;

public interface PaymentCompletedPublisher extends DomainEventPublisher<PaymentCompletedEvent> {
}
