package com.food.ordring.system.order.service.dataaccess.restaurant.repository;

import com.food.ordring.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordring.system.order.service.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    List<RestaurantEntity> findByIdAndProductIdIn(UUID restaurantId, List<UUID> productIds);
}
