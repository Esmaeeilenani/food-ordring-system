package com.food.ordring.system.order.service.domain.outbox.scheduler.payment;

import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
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
@Transactional(readOnly = true)
class SchedulerHelper {
    private final PaymentOutboxRepository paymentOutboxRepository;


    public List<OrderPaymentOutboxMessage> getPaymentOutboxMessagesByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {

        return paymentOutboxRepository.findByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME,
                outboxStatus,
                sagaStatuses
        );

    }


    public Optional<OrderPaymentOutboxMessage> getPaymentOutboxMessageBySagaIdAndSagaStatuses(UUID sagaId,
                                                                                              SagaStatus... sagaStatuses) {
        return paymentOutboxRepository
                .findByTypeAndSagaIdAndSagaStatuses(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }

    @Transactional(readOnly = false)
    public void save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        OrderPaymentOutboxMessage response = paymentOutboxRepository.save(orderPaymentOutboxMessage);

        if (response == null) {
            log.error("Could not save OrderPaymentOutboxMessage with outbox id: {}", orderPaymentOutboxMessage.getId().toString());
            throw new OrderDomainException("Could not save OrderPaymentOutboxMessage with outbox id: " + orderPaymentOutboxMessage.getId().toString());
        }
        log.info("OrderPaymentOutboxMessage saved with outbox id: {}", orderPaymentOutboxMessage.getId().toString());

    }


    public void deletePaymentOutboxMessagesByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus, SagaStatus... sagaStatuses) {
        paymentOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

}
