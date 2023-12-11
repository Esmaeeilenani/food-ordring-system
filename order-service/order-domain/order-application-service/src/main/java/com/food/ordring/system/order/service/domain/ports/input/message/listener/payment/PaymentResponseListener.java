package com.food.ordring.system.order.service.domain.ports.input.message.listener.payment;

import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;

public interface PaymentResponseListener {

    void paymentCompleted(PaymentResponse paymentResponse);
    void paymentCanceled(PaymentResponse paymentResponse);
}
