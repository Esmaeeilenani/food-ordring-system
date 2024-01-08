package com.food.ordring.system.order.service.domain;


import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantResListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordring.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class RestaurantResListenerImpl implements RestaurantResListener {

    private final OrderApprovalSaga orderApprovalSaga;

    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {
        orderApprovalSaga.process(restaurantApprovalResponse);
        log.info("Order is approved for order id : {}", restaurantApprovalResponse.getOrderId());
    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {
        OrderCancelledEvent cancelledEvent = orderApprovalSaga.rollback(restaurantApprovalResponse);
        log.info("Order is Rolling back with failure messages: {} ",
                String.join(FAILURE_MESSAGES_DELIMITER,
                        restaurantApprovalResponse.getFailureMessages()));
        cancelledEvent.fire();
    }
}
