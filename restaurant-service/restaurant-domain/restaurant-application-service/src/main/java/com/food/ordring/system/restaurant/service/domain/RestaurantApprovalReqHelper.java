package com.food.ordring.system.restaurant.service.domain;


import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.ProductId;
import com.food.ordring.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordring.system.restaurant.service.domain.entity.OrderApproval;
import com.food.ordring.system.restaurant.service.domain.entity.Product;
import com.food.ordring.system.restaurant.service.domain.entity.Restaurant;
import com.food.ordring.system.restaurant.service.domain.event.OrderApprovalEvent;
import com.food.ordring.system.restaurant.service.domain.exception.RestaurantNotFoundException;
import com.food.ordring.system.restaurant.service.domain.mapper.RestaurantDataMapper;
import com.food.ordring.system.restaurant.service.domain.ports.output.publisher.OrderApprovedPub;
import com.food.ordring.system.restaurant.service.domain.ports.output.publisher.OrderRejectedPub;
import com.food.ordring.system.restaurant.service.domain.ports.output.repository.OrderApprovalRepo;
import com.food.ordring.system.restaurant.service.domain.ports.output.repository.RestaurantRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RequiredArgsConstructor
@Slf4j
@Component
public class RestaurantApprovalReqHelper {

    private final RestaurantDomainService restaurantDomainService;
    private final RestaurantDataMapper restaurantDataMapper;
    private final RestaurantRepo restaurantRepo;
    private final OrderApprovalRepo orderApprovalRepo;

    private final OrderApprovedPub orderApprovedPublisher;
    private final OrderRejectedPub orderRejectedPublisher;

    @Transactional
    public OrderApprovalEvent persistOrder(RestaurantApprovalRequest restaurantApprovalRequest) {
        log.info("Processing Restaurant Approval Request for order id: {} ", restaurantApprovalRequest.getOrderId());
        List<String> failureMessages = new ArrayList<>();
        Restaurant restaurant = findRestaurant(restaurantApprovalRequest);
        OrderApprovalEvent orderApprovalEvent = restaurantDomainService.validateOrder(restaurant,
                failureMessages,
                orderApprovedPublisher,
                orderRejectedPublisher);

        orderApprovalRepo.save(restaurant.getOrderApproval());

        return orderApprovalEvent;
    }

    private Restaurant findRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {
        Restaurant restaurant = restaurantDataMapper.restaurantApprovalRequestToRestaurant(restaurantApprovalRequest);

        Restaurant savedRestaurant = restaurantRepo.findRestaurantInfo(restaurant)
                .orElseThrow(() -> new RestaurantNotFoundException("restaurant with id: " + restaurant.getId() + " is not found"));
        Map<ProductId, Product> savedProductMap = savedRestaurant.getOrderDetail().getProducts()
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        restaurant.setActive(savedRestaurant.isActive());
        restaurant
                .getOrderDetail()
                .getProducts()
                .stream()
                .filter(p -> savedProductMap.containsKey(p.getId()))
                .forEach(p -> {
                    Product savedProduct = savedProductMap.get(p.getId());
                    p.updateWithConfirmedNamePriceAndAvailability(savedProduct.getName(), savedProduct.getPrice(), savedProduct.isAvailable());
                });
        restaurant.getOrderDetail()
                .setId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())));

        return restaurant;
    }

}
