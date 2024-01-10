package com.food.ordring.system.order.service.domain.outbox.scheduler.payment;

import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestPublisher;
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
public class PaymentOutboxCleanerScheduler implements OutboxScheduler {
    private final SchedulerHelper schedulerHelper;
    private final PaymentRequestPublisher paymentRequestPublisher;


    @Override
    @Scheduled(cron = "@midnight")
    public void processesOutboxMessage() {

        SagaStatus[] sagaStatuses = {SagaStatus.SUCCEEDED,
                SagaStatus.COMPENSATED,
                SagaStatus.FAILED};

        List<OrderPaymentOutboxMessage> paymentOutboxMessages = schedulerHelper
                .getPaymentOutboxMessagesByOutboxStatusAndSagaStatuses(OutboxStatus.COMPLETED,
                        sagaStatuses
                );

        if (paymentOutboxMessages.isEmpty()) {
            return;
        }
        String messagesPayloads = paymentOutboxMessages
                .stream().map(OrderPaymentOutboxMessage::getPayload)
                .collect(Collectors.joining("\n"));

        log.info("Received {} orderPaymentOutboxMessages for clean-up. with payloads {}",
                paymentOutboxMessages.size(),
                messagesPayloads
        );

        schedulerHelper
                .deletePaymentOutboxMessagesByOutboxStatusAndSagaStatuses(OutboxStatus.COMPLETED,
                        sagaStatuses);

        log.info("{} OrderPaymentOutboxMessage deleted!", paymentOutboxMessages.size());


    }
}
