package com.food.ordring.system.restaurant.service.domain.ports.output.repository;

import com.food.ordring.system.restaurant.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepo {

    Optional<Restaurant> findRestaurantInfo(Restaurant restaurant);

}
