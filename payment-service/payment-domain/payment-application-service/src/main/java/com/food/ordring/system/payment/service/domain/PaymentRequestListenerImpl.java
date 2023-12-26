package com.food.ordring.system.payment.service.domain;

import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.ports.input.listener.PaymentRequestListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentRequestListenerImpl implements PaymentRequestListener {


    private final PaymentRequestHelper paymentRequestHelper;


    @Override
    public void completePayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCompletePayment(paymentRequest);
        paymentEvent.fire();
    }


    @Override
    public void cancelPayment(PaymentRequest paymentRequest) {
        PaymentEvent paymentEvent = paymentRequestHelper.persistCancelledPayment(paymentRequest);
        paymentEvent.fire();
    }


}
