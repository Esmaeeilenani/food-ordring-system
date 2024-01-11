package com.food.ordring.system.order.service.domain;


import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.mapper.OrderStatusMapper;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordring.system.order.service.domain.outbox.scheduler.payment.PaymentOutboxHelper;
import com.food.ordring.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {


    private final OrderDataMapper orderDataMapper;

    private final CreateOrderHelper createOrderHelper;

    private final PaymentOutboxHelper paymentOutboxHelper;

    private final OrderStatusMapper orderStatusMapper;


    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        //using new class and not a method inside the current class so that @Transactional can function correctly
        //we need the order to be saved 100% and close the transaction sow we can fire the event with 100% sure that the order is persisted
        OrderCreatedEvent orderCreatedEvent = createOrderHelper.persistOrder(createOrderCommand);
        Order order = orderCreatedEvent.getOrder();
        log.info("order is created with id: {}", order.getId());

        CreateOrderResponse createOrderResponse = orderDataMapper.orderToCreateOrderResponse(order, "Order Created Successfully");
        OrderPaymentEventPayload orderPaymentEventPayload = orderDataMapper.OrderCreatedEventToOrderPaymentPayload(orderCreatedEvent);

        paymentOutboxHelper
                .savePaymentOutboxMessage(orderPaymentEventPayload,
                        order.getOrderStatus(),
                        orderStatusMapper.orderStatusToSagaStatus(order.getOrderStatus()),
                        OutboxStatus.STARTED,
                        UUID.randomUUID());

        return createOrderResponse;
    }


}
