package com.food.ordring.system.order.service.domain;


import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantResListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class RestaurantResListenerImpl implements RestaurantResListener {
    @Override
    public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

    }

    @Override
    public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

    }
}
