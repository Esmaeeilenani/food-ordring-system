package com.food.ordring.system.restaurant.service.domain;

import com.food.ordring.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordring.system.restaurant.service.domain.ports.input.RestaurantApprovalReqListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class RestaurantApprovalReqListenerImpl implements RestaurantApprovalReqListener {

    private final RestaurantApprovalReqHelper restaurantApprovalReqHelper;

    @Override
    public void approveOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        restaurantApprovalReqHelper.persistOrder(restaurantApprovalRequest);
    }
}
