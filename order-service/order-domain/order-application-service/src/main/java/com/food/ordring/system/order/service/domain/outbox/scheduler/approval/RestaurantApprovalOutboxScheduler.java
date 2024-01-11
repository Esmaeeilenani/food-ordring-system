package com.food.ordring.system.order.service.domain.outbox.scheduler.approval;

import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class RestaurantApprovalOutboxScheduler implements OutboxScheduler {

    private final ApprovalOutboxHelper approvalOutboxHelper;

    private final RestaurantApprovalPub restaurantApprovalPub;

    @Override
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processesOutboxMessage() {
        List<OrderApprovalOutboxMessage> approvalOutboxMessages = approvalOutboxHelper.getApprovalOutboxMessagesByOutboxStatusAndSagaStatuses(
                OutboxStatus.STARTED,
                SagaStatus.PROCESSING
        );
        if (approvalOutboxMessages.isEmpty()) {
            return;
        }
        String messagesIds = approvalOutboxMessages
                .stream()
                .map(OrderApprovalOutboxMessage::getId)
                .map(UUID::toString)
                .collect(Collectors.joining(", "));

        log.info("Received {} order approval messages with ids: {}",
                approvalOutboxMessages.size(),
                messagesIds);

        approvalOutboxMessages.forEach(messages -> restaurantApprovalPub.publisher(messages, this::updateOutboxStatus));


    }

    private void updateOutboxStatus(OrderApprovalOutboxMessage orderApprovalOutboxMessage, OutboxStatus outboxStatus) {
        orderApprovalOutboxMessage.setOutboxStatus(outboxStatus);
        approvalOutboxHelper.save(orderApprovalOutboxMessage);
        log.info("orderApprovalOutboxMessage is updated to new outbox status: {}", outboxStatus);

    }
}
