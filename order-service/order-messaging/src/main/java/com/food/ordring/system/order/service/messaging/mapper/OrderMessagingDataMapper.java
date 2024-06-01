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
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderMessagingDataMapper {


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

    public PaymentRequestAvroModel orderPaymentEventToPaymentRequestAvroModel(String sagaId,
                                                                              OrderPaymentEventPayload eventPayload) {

        return PaymentRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setCustomerId(eventPayload.getCustomerId())
                .setOrderId(eventPayload.getOrderId())
                .setPrice(eventPayload.getPrice())
                .setCreatedAt(eventPayload.getCreatedAt().toInstant())
                .setPaymentOrderStatus(PaymentOrderStatus.valueOf(eventPayload.getPaymentOrderStatus()))
                .build();

    }


    public RestaurantApprovalRequestAvroModel orderApprovalEventToApprovalRequestAvroModel(String sagaId, OrderApprovalEventPayload eventPayload) {

        List<Product> products = eventPayload.getProducts()
                .stream()
                .map(eventProduct -> Product.newBuilder()
                        .setId(eventProduct.getId())
                        .setQuantity(eventProduct.getQuantity())
                        .build())
                .toList();

        return RestaurantApprovalRequestAvroModel.newBuilder()
                .setId(UUID.randomUUID().toString())
                .setSagaId(sagaId)
                .setOrderId(eventPayload.getOrderId())
                .setRestaurantOrderStatus(RestaurantOrderStatus.valueOf(eventPayload.getRestaurantOrderStatus()))
                .setProducts(products)
                .setPrice(eventPayload.getPrice())
                .setCreatedAt(eventPayload.getCreatedAt().toInstant())
                .build();
    }


}
