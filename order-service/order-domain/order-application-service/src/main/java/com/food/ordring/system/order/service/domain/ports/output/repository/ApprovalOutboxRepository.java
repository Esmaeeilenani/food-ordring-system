package com.food.ordring.system.order.service.domain.ports.output.repository;

import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApprovalOutboxRepository {

    OrderApprovalOutboxMessage save(OrderApprovalOutboxMessage orderApprovalOutboxMessage);

    List<OrderApprovalOutboxMessage> findByTypeAndOutboxStatusAndSagaStatuses(
            String type,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses
    );

    Optional<OrderApprovalOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(
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
