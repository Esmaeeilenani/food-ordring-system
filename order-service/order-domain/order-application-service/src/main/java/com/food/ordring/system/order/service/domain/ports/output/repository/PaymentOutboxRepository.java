package com.food.ordring.system.order.service.domain.ports.output.repository;

import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentOutboxRepository {
    OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage);

    List<OrderPaymentOutboxMessage> findByTypeAndOutboxStatusAndSagaStatuses(
            String type,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses
    );

    Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(
            String type,
            UUID sagaId,
            SagaStatus... sagaStatuses
    );

    void deleteByTypeAndOutboxStatusAndSagaStatuses(
            String type,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses
    );

}
