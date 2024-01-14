package com.food.ordring.system.order.service.domain.outbox.scheduler.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordring.system.domain.exception.DomainException;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
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
public class PaymentOutboxHelper {
    private final PaymentOutboxRepository paymentOutboxRepository;

    private final ObjectMapper objectMapper;


    @Transactional(readOnly = true)
    public List<OrderPaymentOutboxMessage> getByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {

        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses
        );

    }

    @Transactional(readOnly = true)
    public Optional<OrderPaymentOutboxMessage> getBySagaIdAndSagaStatuses(UUID sagaId,
                                                                          SagaStatus... sagaStatuses) {
        return paymentOutboxRepository
                .findByTypeAndSagaIdAndSagaStatuses(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }


    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = paymentOutboxRepository.save(orderPaymentOutboxMessage);

        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId().toString());
            throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id: " + orderPaymentOutboxMessage.getId().toString());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId().toString());

    }

    public void create(OrderPaymentEventPayload orderPaymentEventPayload,
                       OrderStatus orderStatus,
                       SagaStatus sagaStatus,
                       OutboxStatus outboxStatus,
                       UUID sagaId
                                         ) {

        save(OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(sagaId)
                .createdAt(orderPaymentEventPayload.getCreatedAt())
                .type(ORDER_SAGA_NAME)
                .payload(getPayloadAsJson(orderPaymentEventPayload))
                .orderStatus(orderStatus)
                .sagaStatus(sagaStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String getPayloadAsJson(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            log.error("could not create json for OrderPaymentEventPayload for id: {}",
                    orderPaymentEventPayload.getOrderId(),
                    e);
            throw new DomainException("could not create json for OrderPaymentEventPayload for id: " + orderPaymentEventPayload.getOrderId());
        }

    }

    public void deleteByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

}
