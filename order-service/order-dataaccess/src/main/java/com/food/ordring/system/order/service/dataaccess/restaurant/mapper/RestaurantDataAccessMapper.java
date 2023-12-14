package com.food.ordring.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.ProductId;
import com.food.ordring.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordring.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataAccessMapper {

    public List<UUID> restaurantDomainToProductsIds(Restaurant restaurant) {
        return restaurant
                .getProducts()
                .stream()
                .map(Product::getId)
                .map(ProductId::getValue)
                .toList();
    }


    public Restaurant toRestaurantDomain(List<RestaurantEntity> restaurantsEntities) {
        RestaurantEntity restaurantEntity = restaurantsEntities
                .stream()
                .findFirst()
                .orElseThrow(() -> new RestaurantDataAccessException("Restaurant could not be found!!"));

        List<Product> restaurantProducts = restaurantsEntities
                .stream()
                .map(entity -> {
                    return new Product(entity.getProductId(),
                            entity.getProductName(),
                            new Money(entity.getProductPrice()));
                })
                .toList();


        return Restaurant.builder()
                .id(restaurantEntity.getId())
                .products(restaurantProducts)
                .active(restaurantEntity.getActive())
                .build();
    }
}
