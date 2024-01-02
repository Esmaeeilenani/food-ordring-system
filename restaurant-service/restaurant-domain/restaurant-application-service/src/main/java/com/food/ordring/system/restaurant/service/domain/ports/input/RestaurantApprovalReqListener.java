package com.food.ordring.system.restaurant.service.domain.ports.input;

import com.food.ordring.system.restaurant.service.domain.dto.RestaurantApprovalRequest;

public interface RestaurantApprovalReqListener {

    void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest);


}
