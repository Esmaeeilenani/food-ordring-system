package com.food.ordring.system.order.service.domain.ports.input.message.listener.restaurant;

import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantResListener {

    void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse);
    void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
