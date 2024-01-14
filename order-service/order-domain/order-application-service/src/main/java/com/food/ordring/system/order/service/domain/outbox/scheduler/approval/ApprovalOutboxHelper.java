package com.food.ordring.system.order.service.domain.outbox.scheduler.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordring.system.domain.exception.DomainException;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.ordring.system.saga.order.SagaConstants.ORDER_SAGA_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class ApprovalOutboxHelper {
    private final ApprovalOutboxRepository approvalOutboxRepository;

    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public List<OrderApprovalOutboxMessage> getAllByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getBySagaIdAndSagaStatuses(
            UUID sagaId,
            SagaStatus... sagaStatuses) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatuses(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }


    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        approvalOutboxRepository.save(orderApprovalOutboxMessage);
    }

    public void deleteByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }


    public void create(OrderApprovalEventPayload eventPayload,
                       OrderStatus orderStatus,
                       SagaStatus sagaStatus,
                       OutboxStatus outboxStatus,
                       UUID sagaId) {
        OrderApprovalOutboxMessage approvalOutboxMessage = OrderApprovalOutboxMessage
                .builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .orderStatus(orderStatus)
                .payload(getPayloadAsJson(eventPayload))
                .createdAt(eventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .build();

        save(approvalOutboxMessage);
    }

    private String getPayloadAsJson(OrderApprovalEventPayload eventPayload) {
        try {
            return objectMapper.writeValueAsString(eventPayload);
        } catch (JsonProcessingException e) {
            log.error("could not create json for OrderApprovalEventPayload for id: {}",
                    eventPayload.getOrderId(),
                    e);
            throw new DomainException("could not create json for OrderPaymentEventPayload for id: " + eventPayload.getOrderId());
        }

    }
}
