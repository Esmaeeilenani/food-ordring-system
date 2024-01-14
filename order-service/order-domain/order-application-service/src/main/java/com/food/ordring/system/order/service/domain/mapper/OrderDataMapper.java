package com.food.ordring.system.order.service.domain.mapper;

import com.food.ordring.system.domain.valueobject.*;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordring.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.OrderItem;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalEventProducts;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
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
                        .quantity(orderItem.getQuantity())
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


    public CreateOrderResponse orderToCreateOrderResponse(Order order,String message) {


        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .message(message)
                .build();
    }

    public TrackOrderResponse orderToTrackOrderResponse(Order order) {

        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages())
                .build();
    }

    public OrderPaymentEventPayload OrderCreatedEventToOrderPaymentPayload(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();
        return OrderPaymentEventPayload.builder()
                .customerId(order.getCustomerId().toString())
                .orderId(order.getId().toString())
                .price(order.getPrice().getAmount())
                .createdAt(orderCreatedEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();
    }

    public OrderApprovalEventPayload paidEventToApprovalEventPayload(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();

        List<OrderApprovalEventProducts> orderProducts = order.getOrderItems().stream().map(orderItem ->
                OrderApprovalEventProducts.builder()
                        .id(orderItem.getProduct().getId().getValue().toString())
                        .quantity(orderItem.getQuantity())
                        .build()).toList();

        return  OrderApprovalEventPayload
                .builder()
                .orderId(order.getId().toString())
                .restaurantId(order.getRestaurantId().toString())
                .price(order.getPrice().getAmount())
                .restaurantOrderStatus(RestaurantOrderStatus.PAID.name())
                .products(orderProducts)
                .createdAt(orderPaidEvent.getCreatedAt())
                .build();
    }

    public OrderPaymentEventPayload orderCancelledEventToOrderPaymentEventPayload(OrderCancelledEvent cancelledEvent){

        Order order = cancelledEvent.getOrder();
        return OrderPaymentEventPayload.builder()
                .customerId(order.getCustomerId().toString())
                .orderId(order.getId().toString())
                .price(order.getPrice().getAmount())
                .createdAt(cancelledEvent.getCreatedAt())
                .paymentOrderStatus(PaymentOrderStatus.CANCELLED.name())
                .build();
    }

}
