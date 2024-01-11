package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.domain.valueobject.PaymentStatus;
import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.mapper.OrderStatusMapper;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.outbox.scheduler.approval.ApprovalOutboxHelper;
import com.food.ordring.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;
import com.food.ordring.system.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class OrderPaymentSaga implements SagaStep<PaymentResponse> {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final PaymentOutboxHelper paymentOutboxHelper;

    private final OrderStatusMapper orderStatusMapper;

    private final ApprovalOutboxHelper approvalOutboxHelper;

    private final OrderDataMapper orderDataMapper;

    @Override
    public void process(PaymentResponse paymentResponse) {

        Optional<OrderPaymentOutboxMessage> paymentOutboxMessageOpt = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(paymentResponse.getSagaId()),
                        SagaStatus.STARTED);

        if (paymentOutboxMessageOpt.isEmpty()) {
            log.info("An Outbox message with saga id: {} is already completed for order id: {}",
                    paymentResponse.getSagaId(),
                    paymentResponse.getOrderId());
            return;
        }
        OrderPaymentOutboxMessage paymentOutboxMessage = paymentOutboxMessageOpt.get();

        String orderId = paymentResponse.getOrderId();

        OrderPaidEvent orderPaidEvent = completeOrderPayment(orderId);

        OrderStatus processedOrderStatus = orderPaidEvent.getOrder().getOrderStatus();

        SagaStatus sagaStatus = orderStatusMapper.orderStatusToSagaStatus(processedOrderStatus);

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(paymentOutboxMessage,
                processedOrderStatus,
                sagaStatus
        ));

        approvalOutboxHelper.saveApprovalOutboxMessage(
                orderDataMapper.paidEventToApprovalEventPayload(orderPaidEvent),
                processedOrderStatus,
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(paymentResponse.getSagaId())
        );

        log.info("Order with id: {} is Paid", orderId);

    }


    @Override
    public void rollback(PaymentResponse paymentResponse) {


        Optional<OrderPaymentOutboxMessage> paymentOutboxMessageOpt = paymentOutboxHelper
                .getPaymentOutboxMessageBySagaIdAndSagaStatuses(
                        UUID.fromString(paymentResponse.getSagaId()),
                        getCurrentSagaStatusFromPaymentStatus(paymentResponse.getPaymentStatus())
                );

        if (paymentOutboxMessageOpt.isEmpty()) {
            log.info("An Outbox message with saga id: {} is already roll backed for order id: {}",
                    paymentResponse.getSagaId(),
                    paymentResponse.getOrderId());
            return;
        }

        OrderPaymentOutboxMessage paymentOutboxMessage = paymentOutboxMessageOpt.get();

        String orderId = paymentResponse.getOrderId();

        Order order = rollbackPayment(paymentResponse);
        SagaStatus sagaStatus = orderStatusMapper.orderStatusToSagaStatus(order.getOrderStatus());

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(paymentOutboxMessage,
                order.getOrderStatus(),
                sagaStatus
        ));

        if (paymentResponse.getPaymentStatus().equals(PaymentStatus.CANCELLED)) {
            approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(
                    paymentResponse.getSagaId(),
                    order.getOrderStatus(),
                    sagaStatus
            ));
        }

        log.info("Order with Id: {} is cancelled", orderId);

    }

    private Order rollbackPayment(PaymentResponse paymentResponse) {
        String orderId = paymentResponse.getOrderId();
        log.info("Cancelling order with id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        return orderRepository.save(order);
    }


    private OrderPaidEvent completeOrderPayment(String orderId) {
        log.info("Completing payment for order with id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });

        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order);
        orderRepository.save(order);
        return orderPaidEvent;
    }

    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(OrderPaymentOutboxMessage paymentOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
        paymentOutboxMessage.setProcessedAt(DateAndTimeUtil.zonedDateTimeUTCNow());
        paymentOutboxMessage.setOrderStatus(orderStatus);
        paymentOutboxMessage.setSagaStatus(sagaStatus);

        return paymentOutboxMessage;
    }


    private SagaStatus[] getCurrentSagaStatusFromPaymentStatus(PaymentStatus paymentStatus) {
        return switch (paymentStatus) {
            case COMPLETED -> List.of(SagaStatus.STARTED).toArray(SagaStatus[]::new);
            case CANCELLED -> List.of(SagaStatus.PROCESSING).toArray(SagaStatus[]::new);
            case FAILED -> List.of(SagaStatus.STARTED, SagaStatus.PROCESSING).toArray(SagaStatus[]::new);
        };

    }


    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(String sagaId, OrderStatus orderStatus,
                                                                       SagaStatus sagaStatus) {

        OrderApprovalOutboxMessage approvalOutboxMessage = approvalOutboxHelper
                .getApprovalOutboxMessagesBySagaIdAndSagaStatuses(UUID.fromString(sagaId), SagaStatus.COMPENSATING
                ).orElseThrow(() -> new OrderDomainException("Order Approval outbox message is not found in " +
                        SagaStatus.COMPENSATING.name() + " status"));

        approvalOutboxMessage.setProcessedAt(DateAndTimeUtil.zonedDateTimeUTCNow());
        approvalOutboxMessage.setOrderStatus(orderStatus);
        approvalOutboxMessage.setSagaStatus(sagaStatus);


        return approvalOutboxMessage;
    }

}
