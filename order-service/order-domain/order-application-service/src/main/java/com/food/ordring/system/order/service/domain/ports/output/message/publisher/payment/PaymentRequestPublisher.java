package com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment;

import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface PaymentRequestPublisher {
    void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                 BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallBack);

}
