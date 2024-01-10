package com.food.ordring.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.repository.ApprovalOutboxRepository;
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
class SchedulerHelper {
    private final ApprovalOutboxRepository approvalOutboxRepository;


    @Transactional(readOnly = true)
    public List<OrderApprovalOutboxMessage> getApprovalOutboxMessagesByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {
        return approvalOutboxRepository.findByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }

    @Transactional(readOnly = true)
    public Optional<OrderApprovalOutboxMessage> getApprovalOutboxMessagesBySagaIdAndSagaStatuses(
            UUID sagaId,
            SagaStatus... sagaStatuses) {
        return approvalOutboxRepository.findByTypeAndSagaIdAndSagaStatuses(ORDER_SAGA_NAME, sagaId, sagaStatuses);
    }


    public void save(OrderApprovalOutboxMessage orderApprovalOutboxMessage) {
        approvalOutboxRepository.save(orderApprovalOutboxMessage);
    }

    public void deleteApprovalOutboxMessagesByOutboxStatusAndSagaStatuses(
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {
        approvalOutboxRepository.deleteByTypeAndOutboxStatusAndSagaStatuses(ORDER_SAGA_NAME, outboxStatus, sagaStatuses);
    }


}
