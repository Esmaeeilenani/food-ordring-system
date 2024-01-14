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
public class PaymentOutboxScheduler implements OutboxScheduler {

    private final PaymentOutboxHelper paymentOutboxHelper;
    private final PaymentRequestPublisher paymentRequestPublisher;

    @Override
    @Scheduled(fixedDelayString = "${order-service.outbox-scheduler-fixed-rate}",
            initialDelayString = "${order-service.outbox-scheduler-initial-delay}")
    public void processesOutboxMessage() {
        List<OrderPaymentOutboxMessage> orderPaymentOutboxMessages = paymentOutboxHelper.getByOutboxStatusAndSagaStatuses(OutboxStatus.STARTED,
                SagaStatus.STARTED, SagaStatus.COMPENSATING);
        if (orderPaymentOutboxMessages.isEmpty()) {
            return;
        }

        log.info("Received {} OrderPaymentOutboxMessage with ids: {}, sending to message bus!",
                orderPaymentOutboxMessages.size(),
                orderPaymentOutboxMessages
                        .stream()
                        .map(o -> o.getId().toString())
                        .collect(Collectors.joining(", ")));

        orderPaymentOutboxMessages
                .forEach(outboxMessage ->
                        paymentRequestPublisher.publish(outboxMessage, this::updateOutboxStatus));

        log.info("{} OrderPaymentOutboxMessage is sent to bus!", orderPaymentOutboxMessages.size());

    }


    private void updateOutboxStatus(OrderPaymentOutboxMessage orderPaymentOutboxMessage, OutboxStatus outboxStatus) {
        orderPaymentOutboxMessage.setOutboxStatus(outboxStatus);
        paymentOutboxHelper.save(orderPaymentOutboxMessage);
        log.info("OrderPaymentOutboxMessage updated with outbox status: {}", outboxStatus);

    }
}
