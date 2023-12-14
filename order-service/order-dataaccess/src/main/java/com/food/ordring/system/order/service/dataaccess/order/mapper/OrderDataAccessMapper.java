package com.food.ordring.system.order.service.dataaccess.order.mapper;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.order.service.dataaccess.order.entity.OrderAddress;
import com.food.ordring.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordring.system.order.service.dataaccess.order.entity.OrderItemEntity;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.OrderItem;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.valueobject.OrderItemId;
import com.food.ordring.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordring.system.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class OrderDataAccessMapper {

    public OrderEntity toOrderEntity(Order order) {
        String failureMessages = order.getFailureMessages() == null ? "" : String.join(Order.FAILURE_MESSAGES_DELIMITER, order.getFailureMessages());

        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .customerId(order.getCustomerId().getValue())
                .restaurantId(order.getRestaurantId().getValue())
                .trackingId(order.getTrackingId().getValue())
                .address(toAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .orderItems(toOrderItemsEntity(order.getOrderItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(failureMessages)
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getOrderItems().forEach(i -> i.setOrder(orderEntity));

        return orderEntity;
    }

    public Order toOrderDomain(OrderEntity orderEntity) {

        //wrapping Arrays.asList into ArrayList constructor because Arrays.asList returns Immutable List
        List<String> failureMessages = new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                .split(Order.FAILURE_MESSAGES_DELIMITER)));

        Order order = Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .customerId(new CustomerId(orderEntity.getCustomerId()))
                .restaurantId(new RestaurantId(orderEntity.getRestaurantId()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .deliveryAddress(toAddressDomain(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .orderItems(toOrderItemsDomain(orderEntity.getOrderItems()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(failureMessages)
                .build();


        return order;
    }


    private OrderAddress toAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddress.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .build();
    }

    private StreetAddress toAddressDomain(OrderAddress orderAddress) {
        return new StreetAddress(orderAddress.getId(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity());
    }


    private List<OrderItemEntity> toOrderItemsEntity(List<OrderItem> orderItems) {
        return orderItems.
                stream()
                .map(this::toOrderItemEntity)
                .toList();
    }

    private OrderItemEntity toOrderItemEntity(OrderItem orderItem) {
        return OrderItemEntity.builder()
                .id(orderItem.getId().getValue())
                .productId(orderItem.getProduct().getId().getValue())
                .price(orderItem.getPrice().getAmount())
                .quantity(orderItem.getQuantity())
                .subTotal(orderItem.getSubTotal().getAmount())
                .build();

    }

    private List<OrderItem> toOrderItemsDomain(List<OrderItemEntity> orderItemEntities) {
        return orderItemEntities.
                stream()
                .map(this::toOrderItemDomain)
                .toList();
    }

    private OrderItem toOrderItemDomain(OrderItemEntity orderItemEntity) {
        return OrderItem.builder()
                .orderItemId(new OrderItemId(orderItemEntity.getId()))
                .product(new Product(orderItemEntity.getProductId()))
                .price(new Money(orderItemEntity.getPrice()))
                .quantity(orderItemEntity.getQuantity())
                .subTotal(new Money(orderItemEntity.getSubTotal()))
                .build();

    }

}
