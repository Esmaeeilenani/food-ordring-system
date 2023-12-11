package com.food.ordring.system.order.service.domain.mapper;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.OrderItem;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
        List<Product> products = createOrderCommand.getItems()
                .stream()
                .map(orderItem -> new Product(orderItem.getProductId()))
                .toList();
        return Restaurant.builder()
                .id(createOrderCommand.getRestaurantId())
                .products(products)
                .build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        List<Product> products = createOrderCommand.getItems()
                .stream()
                .map(orderItem -> new Product(orderItem.getProductId()))
                .toList();
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .restaurantId(new RestaurantId(createOrderCommand.getRestaurantId()))
                .deliveryAddress(orderAddresToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .orderItems(orderItemsToEntities(createOrderCommand.getItems()))
                .build();
    }

    private List<OrderItem> orderItemsToEntities(List<com.food.ordring.system.order.service.domain.dto.create.OrderItem> items) {
        return items
                .stream()
                .map(orderItem -> OrderItem.builder()
                        .product(new Product(orderItem.getProductId()))
                        .price(new Money(orderItem.getPrice()))
                        .quantity(orderItem.getQauntity())
                        .subTotal(new Money(orderItem.getSubTotal()))
                        .build())
                .toList();
    }

    private StreetAddress orderAddresToStreetAddress(OrderAddress address) {
        return new StreetAddress(
                UUID.randomUUID(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity());
    }


    public CreateOrderResponse orderToCreateOrderResponse(Order order) {


        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
