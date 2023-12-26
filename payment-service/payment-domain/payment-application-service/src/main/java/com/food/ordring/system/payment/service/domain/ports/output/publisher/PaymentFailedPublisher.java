package com.food.ordring.system.payment.service.domain.ports.output.publisher;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;

public interface PaymentFailedPublisher extends DomainEventPublisher<PaymentFailedEvent> {
}
