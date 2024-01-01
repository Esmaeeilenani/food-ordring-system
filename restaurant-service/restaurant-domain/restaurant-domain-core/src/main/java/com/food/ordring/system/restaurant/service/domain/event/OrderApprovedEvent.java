package com.food.ordring.system.restaurant.service.domain.event;


import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.restaurant.service.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;

public class OrderApprovedEvent extends OrderApprovalEvent {

    private final DomainEventPublisher<OrderApprovedEvent> publisher;

    public OrderApprovedEvent(OrderApproval orderApproval,
                              RestaurantId restaurantId,
                              List<String> failureMessages,
                              ZonedDateTime createdAt,
                              DomainEventPublisher<OrderApprovedEvent> publisher
    ) {
        super(orderApproval, restaurantId, failureMessages, createdAt);
        this.publisher = publisher;
    }


    @Override
    public void fire() {
        this.publisher.publish(this);
    }
}
