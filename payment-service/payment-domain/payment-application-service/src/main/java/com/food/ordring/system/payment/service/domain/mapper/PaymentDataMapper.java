package com.food.ordring.system.payment.service.domain.mapper;

import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentDataMapper {
    public Payment paymentRequestToPayment(PaymentRequest paymentRequest) {
        return Payment.builder()
                .customerId(new CustomerId(UUID.fromString(paymentRequest.getCustomerId())))
                .orderId(new OrderId(UUID.fromString(paymentRequest.getOrderId())))
                .price(new Money(paymentRequest.getPrice()))
                .build();
    }
}
