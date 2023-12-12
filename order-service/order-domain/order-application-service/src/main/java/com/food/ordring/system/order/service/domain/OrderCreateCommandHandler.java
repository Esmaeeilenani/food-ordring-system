package com.food.ordring.system.order.service.domain;


import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {


    private final OrderDataMapper orderDataMapper;

    private final CreateOrderHelper createOrderHelper;

    private final OrderCreatedPublisher orderCreatedPublisher;

    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        //using new class and not a method inside the current class so that @Transactional can function correctly
        //we need the order to be saved 100% and close the transaction sow we can fire the event with 100% sure that the order is persisted
        OrderCreatedEvent orderCreatedEvent = createOrderHelper.persistOrder(createOrderCommand);
        log.info("order is created with id: {}", orderCreatedEvent.getOrder().getId());
        orderCreatedPublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }


}
