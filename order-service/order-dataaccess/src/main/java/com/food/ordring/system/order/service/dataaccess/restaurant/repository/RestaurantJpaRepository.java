package com.food.ordring.system.order.service.dataaccess.restaurant.repository;

import com.food.ordring.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordring.system.order.service.dataaccess.restaurant.entity.RestaurantEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, RestaurantEntityId> {

    @Query("select r from RestaurantEntity r where r.id=:id and r.productId in (:productIds)")
    List<RestaurantEntity> findByIdAndProductIdIn(@Param("id") UUID restaurantId,@Param("productIds") List<UUID> productIds);
}
