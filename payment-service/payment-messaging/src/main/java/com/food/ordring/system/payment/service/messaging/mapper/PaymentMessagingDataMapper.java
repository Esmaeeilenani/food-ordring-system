package com.food.ordring.system.payment.service.messaging.mapper;

import com.food.ordring.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordring.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordring.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import com.food.ordring.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMessagingDataMapper {

    public PaymentResponseAvroModel paymentCompletedEventToPaymentResponseAvroModel(PaymentCompletedEvent completedEvent) {
        return paymentEventToPaymentResponseAvroModel(completedEvent);
    }

    public PaymentResponseAvroModel paymentCancelledEventToPaymentResponseAvroModel(PaymentCancelledEvent paymentCancelledEvent) {
        return paymentEventToPaymentResponseAvroModel(paymentCancelledEvent);
    }

    public PaymentResponseAvroModel paymentFailedEventToPaymentResponseAvroModel(PaymentFailedEvent failedEvent) {
        return paymentEventToPaymentResponseAvroModel(failedEvent);
    }


    public PaymentRequest paymentRequestAvroModelToPaymentRequest(PaymentRequestAvroModel avroModel) {
        return PaymentRequest.builder()
                .id(avroModel.getId())
                .sagaId(avroModel.getSagaId())
                .customerId(avroModel.getCustomerId())
                .orderId(avroModel.getOrderId())
                .price(avroModel.getPrice())
                .createdAt(avroModel.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.valueOf(avroModel.getPaymentOrderStatus().name()))
                .build();
    }

    private PaymentResponseAvroModel paymentEventToPaymentResponseAvroModel(PaymentEvent paymentEvent) {
        Payment payment = paymentEvent.getPayment();
        return PaymentResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setPaymentId(payment.getId().toString())
                .setCustomerId(payment.getCustomerId().toString())
                .setOrderId(payment.getOrderId().toString())
                .setPrice(payment.getPrice().getAmount())
                .setCreatedAt(paymentEvent.getCreatedAt().toInstant())
                .setPaymentStatus(PaymentStatus.valueOf(payment.getPaymentStatus().name()))
                .setFailureMessages(paymentEvent.getFailureMessages())
                .build();
    }

}
