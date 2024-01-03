package com.food.ordring.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordring.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;


import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    @Override
    public Restaurant save(Restaurant restaurant) {

        return null;
    }

    @Override
    public Optional<Restaurant> findRestaurantInfo(Restaurant restaurant) {
        List<UUID> productsIds = restaurantDataAccessMapper.restaurantDomainToProductsIds(restaurant);
        List<RestaurantEntity> restaurantEntities = restaurantJpaRepository.findByIdAndProductIdIn(restaurant.getId().getValue(), productsIds);
        return Optional.ofNullable(restaurantDataAccessMapper.toRestaurantDomain(restaurantEntities));
    }
}
