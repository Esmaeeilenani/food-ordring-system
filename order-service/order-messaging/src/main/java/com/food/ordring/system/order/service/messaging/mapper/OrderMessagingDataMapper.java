package com.food.ordring.system.order.service.messaging.mapper;

import com.food.ordring.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordring.system.domain.valueobject.PaymentStatus;
import com.food.ordring.system.kafka.order.avro.model.*;
import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordring.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderMessagingDataMapper {

    public PaymentRequestAvroModel orderCreatedEventToPaymentRequestAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.PENDING)
                .build();
    }

    public PaymentRequestAvroModel orderCancelledEventToPaymentRequestAvroModel(OrderCancelledEvent OrderCancelledEvent) {
        Order order = OrderCancelledEvent.getOrder();

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setCustomerId(order.getCustomerId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(OrderCancelledEvent.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.CANCELLED)
                .build();
    }

    public RestaurantApprovalRequestAvroModel OrderPaidEventToRestaurantApprovalRequestAvroModel(OrderPaidEvent orderPaidEvent) {
        Order order = orderPaidEvent.getOrder();

        List<Product> orderProducts = order.getOrderItems().stream().map(orderItem ->
                Product.newBuilder()
                        .setId(orderItem.getProduct().getId().getValue().toString())
                        .setQuantity(orderItem.getQuantity())
                        .build()).toList();
        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId("")
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantId(order.getRestaurantId().getValue().toString())
                .setOrderId(order.getId().getValue().toString())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(order.getOrderStatus().name()))
                .setProducts(orderProducts)
                .setPrice(order.getPrice().getAmount())
                .setCreatedAt(orderPaidEvent.getCreatedAt().toInstant())
                .setRestaurantOrderStatus(RestaurantOrderStatus.PAID)
                .build();
    }

    public PaymentResponse paymentResponseAvroToPaymentResponse(PaymentResponseAvroModel paymentResponseAvroModel) {
        return PaymentResponse.builder()
                .id(paymentResponseAvroModel.getId())
                .sagaId(paymentResponseAvroModel.getSagaId())
                .paymentId(paymentResponseAvroModel.getPaymentId())
                .orderId(paymentResponseAvroModel.getOrderId())
                .customerId(paymentResponseAvroModel.getCustomerId())
                .price(paymentResponseAvroModel.getPrice())
                .createdAt(paymentResponseAvroModel.getCreatedAt())
                .paymentStatus(PaymentStatus.valueOf(paymentResponseAvroModel.getPaymentStatus().name()))
                .failureMessages(paymentResponseAvroModel.getFailureMessages())
                .build();
    }

    public RestaurantApprovalResponse restaurantApprovalAvroToRestaurantApprovalResponse(RestaurantApprovalResponseAvroModel rarAvroModel) {
        return RestaurantApprovalResponse.builder()
                .id(rarAvroModel.getId())
                .sagaId(rarAvroModel.getSagaId())
                .orderId(rarAvroModel.getOrderId())
                .restaurantId(rarAvroModel.getRestaurantId())
                .createdAt(rarAvroModel.getCreatedAt())
                .orderApprovalStatus(OrderApprovalStatus.valueOf(rarAvroModel.getOrderApprovalStatus().name()))
                .failureMessages(rarAvroModel.getFailureMessages())
                .build();
    }
}
