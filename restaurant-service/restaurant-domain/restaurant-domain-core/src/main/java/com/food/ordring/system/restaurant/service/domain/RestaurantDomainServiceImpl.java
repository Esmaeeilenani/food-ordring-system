package com.food.ordring.system.restaurant.service.domain;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordring.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordring.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordring.system.restaurant.service.domain.event.OrderApprovedEvent;
import com.food.ordring.system.restaurant.service.domain.event.OrderRejectedEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RestaurantDomainServiceImpl implements RestaurantDomainService {
    @Override
    public OrderApprovalEvent validateOrder(Restaurant restaurant,
                                            List<String> failureMessages,
                                            DomainEventPublisher<OrderApprovedEvent> orderApprovedEventPublisher,
                                            DomainEventPublisher<OrderRejectedEvent> orderRejectedEventPublisher) {

        log.info("Validate Order with id: {}", restaurant.getOrderDetail().getId());
        restaurant.validateOrder(failureMessages);

        if (!failureMessages.isEmpty()) {
            log.info("order is rejected for order id: {}", restaurant.getOrderDetail().getId());
            restaurant.constructOrderApproval(OrderApprovalStatus.REJECTED);
            return new OrderRejectedEvent(restaurant.getOrderApproval(),
                    restaurant.getId(),
                    failureMessages,
                    DateAndTimeUtil.zonedDateTimeUTCNow(),
                    orderRejectedEventPublisher);
        }


        log.info("Order is approved with id : {}", restaurant.getOrderDetail().getId());
        restaurant.constructOrderApproval(OrderApprovalStatus.APPROVED);

        return new OrderApprovedEvent(restaurant.getOrderApproval(),
                restaurant.getId(),
                failureMessages,
                DateAndTimeUtil.zonedDateTimeUTCNow(),
                orderApprovedEventPublisher);
    }
}
