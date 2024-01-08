package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.event.EmptyEvent;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant.OrderPaidPublisher;
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
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    private final OrderPaidPublisher orderPaidPublisher;

    @Override
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        String orderId = paymentResponse.getOrderId();
        log.info("Completing payment for order with id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order, orderPaidPublisher);
        orderRepository.save(order);
        return orderPaidEvent;
    }

    @Override
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        String orderId = paymentResponse.getOrderId();
        log.info("Cancelling order with id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> {
                    log.info("order with id: {} is not found", orderId);
                    throw new OrderNotFoundException("order with id " + orderId + " is not found");
                });
        orderDomainService.cancelOrder(order,paymentResponse.getFailureMessages());
        orderRepository.save(order);
        log.info("Order with Id: {} is cancelled", orderId);
        return EmptyEvent.INSTANCE;
    }
}
