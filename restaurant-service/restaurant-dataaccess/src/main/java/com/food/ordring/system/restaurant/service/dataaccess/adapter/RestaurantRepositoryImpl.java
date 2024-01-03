package com.food.ordring.system.restaurant.service.dataaccess.adapter;


import com.food.ordering.system.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordring.system.restaurant.service.dataaccess.mapper.RestaurantDataAccessMapper;
import com.food.ordring.system.restaurant.service.domain.entity.Restaurant;

import com.food.ordring.system.restaurant.service.domain.ports.output.repository.RestaurantRepo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class RestaurantRepositoryImpl implements RestaurantRepo {

    private final RestaurantJpaRepository restaurantJpaRepository;
    private final RestaurantDataAccessMapper restaurantDataAccessMapper;

    public RestaurantRepositoryImpl(RestaurantJpaRepository restaurantJpaRepository,
                                    RestaurantDataAccessMapper restaurantDataAccessMapper) {
        this.restaurantJpaRepository = restaurantJpaRepository;
        this.restaurantDataAccessMapper = restaurantDataAccessMapper;
    }

    @Override
    public Optional<Restaurant> findRestaurantInfo(Restaurant restaurant) {
        List<UUID> restaurantProducts =
                restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
        List<RestaurantEntity> restaurantEntities = restaurantJpaRepository
                .findByIdAndProductIdIn(restaurant.getId().getValue(),
                        restaurantProducts);
        return Optional.ofNullable(restaurantDataAccessMapper.restaurantEntityToRestaurant(restaurantEntities));
    }
}
