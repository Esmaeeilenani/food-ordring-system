package com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant;

import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.outbox.OutboxStatus;

import java.util.function.BiConsumer;

public interface RestaurantApprovalPub {
    void publisher(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                   BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallBack);


}
