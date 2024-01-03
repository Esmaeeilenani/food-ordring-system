package com.food.ordring.system.payment.service.messaging.publisher;

import com.food.ordring.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentCancelledPublisher;
import com.food.ordring.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentCancelledKafkaPublisher implements PaymentCancelledPublisher {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;
    private final PaymentEventKafkaPublisher<PaymentCancelledEvent> paymentEventKafkaPublisher;

    @Override
    public void publish(PaymentCancelledEvent domainEvent) {
        paymentEventKafkaPublisher.publish(domainEvent, paymentMessagingDataMapper::paymentCancelledEventToPaymentResponseAvroModel);
    }
}