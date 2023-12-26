package com.food.ordring.system.payment.service.domain.ports.input.listener;

import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;

public interface PaymentRequestListener {

    void completePayment(PaymentRequest paymentRequest);
    void cancelPayment(PaymentRequest paymentRequest);
}
