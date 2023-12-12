package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.exception.OrderNotFoundException;
import com.food.ordring.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordring.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderRepository orderRepository;


    @Transactional(readOnly = true)
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        Optional<Order> orderOpt = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));

        if (orderOpt.isEmpty()) {
            log.warn("could not found order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("order with tracking id: " + trackOrderQuery.getOrderTrackingId() + " not found");
        }


        return orderDataMapper.orderToTrackOrderResponse(orderOpt.get());
    }
}
