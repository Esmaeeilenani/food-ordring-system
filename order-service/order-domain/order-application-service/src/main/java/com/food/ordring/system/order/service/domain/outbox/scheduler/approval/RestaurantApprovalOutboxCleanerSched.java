package com.food.ordring.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalPub;
import com.food.ordring.system.outbox.OutboxScheduler;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class RestaurantApprovalOutboxCleanerSched implements OutboxScheduler {

    private final SchedulerHelper schedulerHelper;

    private final RestaurantApprovalPub restaurantApprovalPub;


    @Override
    @Scheduled(cron = "@midnight")
    public void processesOutboxMessage() {
        SagaStatus[] sagaStatuses = {
                SagaStatus.SUCCEEDED,
                SagaStatus.COMPENSATED,
                SagaStatus.FAILED
        };
        List<OrderApprovalOutboxMessage> approvalOutboxMessages = schedulerHelper.getApprovalOutboxMessagesByOutboxStatusAndSagaStatuses(
                OutboxStatus.COMPLETED,
                sagaStatuses
        );

        if (approvalOutboxMessages.isEmpty()) {
            return;
        }
        String messagesPayloads = approvalOutboxMessages
                .stream().map(OrderApprovalOutboxMessage::getPayload)
                .collect(Collectors.joining("\n"));

        log.info("Received {} OrderApprovalOutboxMessage for clean-up. with payloads {}",
                approvalOutboxMessages.size(),
                messagesPayloads
        );

        schedulerHelper.deleteApprovalOutboxMessagesByOutboxStatusAndSagaStatuses(
                OutboxStatus.COMPLETED,
                sagaStatuses
        );


    }
}
