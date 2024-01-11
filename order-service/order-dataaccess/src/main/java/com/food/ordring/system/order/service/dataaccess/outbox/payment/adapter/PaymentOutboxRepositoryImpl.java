package com.food.ordring.system.order.service.dataaccess.outbox.payment.adapter;

import com.food.ordring.system.order.service.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordring.system.order.service.dataaccess.outbox.payment.exception.PaymentOutboxNotFoundException;
import com.food.ordring.system.order.service.dataaccess.outbox.payment.mapper.PaymentOutboxDataAccessMapper;
import com.food.ordring.system.order.service.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;

import com.food.ordring.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {

    private final PaymentOutboxJpaRepository paymentOutboxJpaRepository;
    private final PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper;

    public PaymentOutboxRepositoryImpl(PaymentOutboxJpaRepository paymentOutboxJpaRepository,
                                       PaymentOutboxDataAccessMapper paymentOutboxDataAccessMapper) {
        this.paymentOutboxJpaRepository = paymentOutboxJpaRepository;
        this.paymentOutboxDataAccessMapper = paymentOutboxDataAccessMapper;
    }

    @Override
    public OrderPaymentOutboxMessage save(OrderPaymentOutboxMessage orderPaymentOutboxMessage) {
        return paymentOutboxDataAccessMapper
                .paymentOutboxEntityToOrderPaymentOutboxMessage(paymentOutboxJpaRepository
                        .save(paymentOutboxDataAccessMapper
                                .orderPaymentOutboxMessageToOutboxEntity(orderPaymentOutboxMessage)));
    }

    @Override
    public List<OrderPaymentOutboxMessage> findByTypeAndOutboxStatusAndSagaStatuses(
            String sagaType,
            OutboxStatus outboxStatus,
            SagaStatus... sagaStatuses) {
        List<PaymentOutboxEntity> paymentOutboxEntities = paymentOutboxJpaRepository.findByTypeAndOutboxStatusAndSagaStatusIn(sagaType,
                outboxStatus,
                Arrays.asList(sagaStatuses));

        if (paymentOutboxEntities.isEmpty()) {
           throw new PaymentOutboxNotFoundException("Payment outbox object " +
                    "could not be found for saga type " + sagaType);
        }

        return paymentOutboxEntities.stream()
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage)
                .toList();
    }

    @Override
    public Optional<OrderPaymentOutboxMessage> findByTypeAndSagaIdAndSagaStatuses(String type,
                                                                                UUID sagaId,
                                                                                SagaStatus... sagaStatus) {
        return paymentOutboxJpaRepository
                .findByTypeAndSagaIdAndSagaStatusIn(type, sagaId, Arrays.asList(sagaStatus))
                .map(paymentOutboxDataAccessMapper::paymentOutboxEntityToOrderPaymentOutboxMessage);
    }

    @Override
    public void deleteByTypeAndOutboxStatusAndSagaStatuses(String type, OutboxStatus outboxStatus, SagaStatus... sagaStatus) {
        paymentOutboxJpaRepository.deleteByTypeAndOutboxStatusAndSagaStatusIn(type, outboxStatus,
                Arrays.asList(sagaStatus));
    }
}
