package com.food.ordring.system.order.service.dataaccess.order.adapter;

import com.food.ordring.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordring.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordring.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    @Override
    public Order save(Order order) {
        OrderEntity orderEntity = orderDataAccessMapper.toOrderEntity(order);
        orderJpaRepository.save(orderEntity);
        return orderDataAccessMapper.toOrderDomain(orderEntity);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::toOrderDomain);
    }
}
