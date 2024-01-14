package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse> {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    private final PaymentOutboxHelper paymentOutboxHelper;

    private final ApprovalOutboxHelper approvalOutboxHelper;

    private final OrderDataMapper orderDataMapper;

    private final OrderStatusMapper orderStatusMapper;

    @Override
    public void process(RestaurantApprovalResponse restaurantApprovalResponse) {


        Optional<OrderApprovalOutboxMessage> approvalOutboxMessageOpt = approvalOutboxHelper.getBySagaIdAndSagaStatuses(UUID.fromString(restaurantApprovalResponse.getSagaId()),
                SagaStatus.PROCESSING
        );

        if (approvalOutboxMessageOpt.isEmpty()) {
            log.info("An outbox message with id: {} is already processed", restaurantApprovalResponse.getSagaId());
            return;
        }
        OrderApprovalOutboxMessage approvalOutboxMessage = approvalOutboxMessageOpt.get();
        Order order = approveOrder(restaurantApprovalResponse);

        SagaStatus sagaStatus = orderStatusMapper.orderStatusToSagaStatus(order.getOrderStatus());

        approvalOutboxHelper
                .save(getUpdatedApprovalOutboxMessage(approvalOutboxMessage, order.getOrderStatus()
                        , sagaStatus));

        paymentOutboxHelper.save(getUpdatedPaymentOutboxMessage(restaurantApprovalResponse.getSagaId(),
                order.getOrderStatus(),
                sagaStatus));

        log.info("order with id: {} is approved", order.getId().toString());

    }


    @Override
    public void rollback(RestaurantApprovalResponse restaurantApprovalResponse) {

        Optional<OrderApprovalOutboxMessage> approvalOutboxMessageOpt = approvalOutboxHelper.getBySagaIdAndSagaStatuses(UUID.fromString(restaurantApprovalResponse.getSagaId()),
                SagaStatus.PROCESSING
        );

        if (approvalOutboxMessageOpt.isEmpty()) {
            log.info("An outbox message with id: {} is already roll backed", restaurantApprovalResponse.getSagaId());
            return;
        }
        OrderApprovalOutboxMessage approvalOutboxMessage = approvalOutboxMessageOpt.get();

        OrderCancelledEvent orderCancelledEvent = rollBackOrder(restaurantApprovalResponse);
        Order order = orderCancelledEvent.getOrder();

        SagaStatus sagaStatus = orderStatusMapper.orderStatusToSagaStatus(order.getOrderStatus());

        approvalOutboxHelper.save(getUpdatedApprovalOutboxMessage(approvalOutboxMessage,
                order.getOrderStatus(), sagaStatus));

        paymentOutboxHelper.create(orderDataMapper.orderCancelledEventToOrderPaymentEventPayload(orderCancelledEvent),
                order.getOrderStatus(),
                sagaStatus,
                OutboxStatus.STARTED,
                UUID.fromString(restaurantApprovalResponse.getSagaId()));

        log.info("Cancelling order with id: {}", order.getId().toString());


    }


    private Order approveOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();
        log.info("Approving order with id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });

        orderDomainService.approveOrder(order);
        return orderRepository.save(order);
    }


    private OrderApprovalOutboxMessage getUpdatedApprovalOutboxMessage(OrderApprovalOutboxMessage approvalOutboxMessage, OrderStatus orderStatus, SagaStatus sagaStatus) {
        approvalOutboxMessage.setProcessedAt(DateAndTimeUtil.zonedDateTimeUTCNow());
        approvalOutboxMessage.setOrderStatus(orderStatus);
        approvalOutboxMessage.setSagaStatus(sagaStatus);
        return approvalOutboxMessage;
    }


    private OrderPaymentOutboxMessage getUpdatedPaymentOutboxMessage(String sagaId, OrderStatus orderStatus,
                                                                     SagaStatus sagaStatus) {

        OrderPaymentOutboxMessage paymentOutboxMessage = paymentOutboxHelper.getBySagaIdAndSagaStatuses(UUID.fromString(sagaId), sagaStatus)
                .orElseThrow(() -> new OrderDomainException("Payment outbox message is not found in " + sagaStatus.name()));

        paymentOutboxMessage.setProcessedAt(DateAndTimeUtil.zonedDateTimeUTCNow());
        paymentOutboxMessage.setOrderStatus(orderStatus);
        paymentOutboxMessage.setSagaStatus(sagaStatus);
        return paymentOutboxMessage;
    }


    private OrderCancelledEvent rollBackOrder(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();


        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });

        OrderCancelledEvent orderCancelledEvent = orderDomainService.cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages());
        orderRepository.save(order);
        return orderCancelledEvent;

    }


}
