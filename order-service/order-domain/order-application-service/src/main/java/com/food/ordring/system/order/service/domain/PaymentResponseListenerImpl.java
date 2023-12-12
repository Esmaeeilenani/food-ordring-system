package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentResponseListenerImpl implements PaymentResponseListener {
    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {

    }

    @Override
    public void paymentCanceled(PaymentResponse paymentResponse) {

    }
}
