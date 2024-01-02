package com.food.ordring.system.restaurant.service.domain.dto;

import com.food.ordring.system.domain.valueobject.RestaurantOrderStatus;
import com.food.ordring.system.restaurant.service.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RestaurantApprovalRequest {

    private String id;
    private String sagaId;
    private String restaurantId;
    private String orderId;
    private RestaurantOrderStatus restaurantOrderStatus;
    private List<Product> products;
    private BigDecimal price;
    private Instant createdAt;


}
