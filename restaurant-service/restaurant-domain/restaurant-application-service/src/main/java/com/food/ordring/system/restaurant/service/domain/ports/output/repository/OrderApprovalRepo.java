package com.food.ordring.system.restaurant.service.domain.ports.output.repository;

import com.food.ordring.system.restaurant.service.domain.entity.OrderApproval;

public interface OrderApprovalRepo {

    OrderApproval save(OrderApproval orderApproval);
}
