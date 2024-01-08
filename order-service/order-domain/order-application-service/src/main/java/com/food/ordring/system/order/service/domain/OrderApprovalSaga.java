package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.event.EmptyEvent;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPublisher;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.saga.SagaStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class OrderApprovalSaga implements SagaStep<RestaurantApprovalResponse,
        EmptyEvent,
        OrderCancelledEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final OrderCancelledPublisher orderCancelledPublisher;

    @Override
    public EmptyEvent process(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();
        log.info("Approving order with id: {}", orderId);

        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });

        orderDomainService.approveOrder(order);
        orderRepository.save(order);
        log.info("order with id: {} is approved", orderId);
        return EmptyEvent.INSTANCE;
    }

    @Override
    public OrderCancelledEvent rollback(RestaurantApprovalResponse restaurantApprovalResponse) {
        String orderId = restaurantApprovalResponse.getOrderId();
        log.info("Cancelling order with id: {}", orderId);

        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });

        OrderCancelledEvent orderCancelledEvent = orderDomainService.cancelOrderPayment(order, restaurantApprovalResponse.getFailureMessages(), orderCancelledPublisher);
        orderRepository.save(order);

        return orderCancelledEvent;
    }
}
