package com.food.ordring.system.restaurant.service.domain.event;


import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderRejectedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderRejectedEvent> publisher;

    public OrderRejectedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              DomainEventPublisher<OrderRejectedEvent> publisher
    ) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }


    @Override
    public void fire() {
        this.publisher.publish(this);
    }
}
