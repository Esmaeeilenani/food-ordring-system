package com.food.ordring.system.restaurant.service.domain.mapper;

import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.restaurant.service.domain.dto.RestaurantApprovalRequest;
import com.food.ordring.system.restaurant.service.domain.entity.OrderDetail;
import com.food.ordring.system.restaurant.service.domain.entity.Product;
import com.food.ordring.system.restaurant.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RestaurantDataMapper {


    public Restaurant restaurantApprovalRequestToRestaurant(RestaurantApprovalRequest restaurantApprovalRequest) {

        List<Product> products = restaurantApprovalRequest.getProducts()
                .stream()
                .map(p-> Product.builder()
                        .productId(p.getId())
                        .quantity(p.getQuantity())
                        .build())
                .toList();

        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(new OrderId(UUID.fromString(restaurantApprovalRequest.getOrderId())))
                .products(products)
                .totalAmount(new Money(restaurantApprovalRequest.getPrice()))
                .orderStatus(OrderStatus.valueOf(restaurantApprovalRequest.getRestaurantOrderStatus().name()))
                .build();

        return Restaurant.builder()
                .restaurantId(new RestaurantId(UUID.fromString(restaurantApprovalRequest.getRestaurantId())))
                .orderDetail(orderDetail)
                .build();
    }
}
