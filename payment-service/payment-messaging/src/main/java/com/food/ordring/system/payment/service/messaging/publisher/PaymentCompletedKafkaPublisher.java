package com.food.ordring.system.payment.service.messaging.publisher;


import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentCompletedPublisher;
import com.food.ordring.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class PaymentCompletedKafkaPublisher implements PaymentCompletedPublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final PaymentEventKafkaPublisher<PaymentCompletedEvent> publisher;

    @Override
    public void publish(PaymentCompletedEvent domainEvent) {
        publisher.publish(domainEvent, paymentMessagingDataMapper::paymentCompletedEventToPaymentResponseAvroModel);
    }
}
