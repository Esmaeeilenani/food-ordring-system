package com.food.ordring.system.restaurant.service.dataaccess.adapter;

import com.food.ordring.system.restaurant.service.dataaccess.mapper.RestaurantDataAccessMapper;
import com.food.ordring.system.restaurant.service.dataaccess.repository.OrderApprovalJpaRepository;
import com.food.ordring.system.restaurant.service.domain.entity.OrderApproval;

import com.food.ordring.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepo;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalRepositoryImpl implements OrderApprovalRepo {

    private final OrderApprovalJpaRepository orderApprovalJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public OrderApprovalRepositoryImpl(OrderApprovalJpaRepository orderApprovalJpaRepository,
                                       RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.orderApprovalJpaRepository = orderApprovalJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public OrderApproval save(OrderApproval orderApproval) {
        return restaurantDataAccessMapper
                .orderApprovalEntityToOrderApproval(orderApprovalJpaRepository
                        .save(restaurantDataAccessMapper.orderApprovalToOrderApprovalEntity(orderApproval)));
    }

}
