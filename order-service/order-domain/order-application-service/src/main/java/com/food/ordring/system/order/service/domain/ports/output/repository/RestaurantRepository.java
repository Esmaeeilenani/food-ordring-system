package com.food.ordring.system.order.service.domain.ports.output.repository;

import com.food.ordring.system.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
   Optional<Restaurant> findRestaurantInfo(Restaurant restaurant);

}
