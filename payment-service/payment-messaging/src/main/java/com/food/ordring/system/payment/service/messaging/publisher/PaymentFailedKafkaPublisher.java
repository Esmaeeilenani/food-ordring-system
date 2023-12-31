package com.food.ordring.system.payment.service.messaging.publisher;

import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentFailedPublisher;
import com.food.ordring.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentFailedKafkaPublisher implements PaymentFailedPublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final PaymentEventKafkaPublisher<PaymentFailedEvent> publisher;

    @Override
    public void publish(PaymentFailedEvent domainEvent) {
        publisher.publish(domainEvent, paymentMessagingDataMapper::paymentFailedEventToPaymentResponseAvroModel);
    }
}
