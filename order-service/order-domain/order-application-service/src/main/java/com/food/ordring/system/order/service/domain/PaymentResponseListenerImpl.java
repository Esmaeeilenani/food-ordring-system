package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.dto.message.PaymentResponse;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.food.ordring.system.order.service.domain.entity.Order.FAILURE_MESSAGES_DELIMITER;

@Validated
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentResponseListenerImpl implements PaymentResponseListener {

    private final OrderPaymentSaga orderPaymentSaga;

    @Override
    public void paymentCompleted(PaymentResponse paymentResponse) {
        orderPaymentSaga.process(paymentResponse);
        log.info("Order payment saga process is completed with order id: {}", paymentResponse.getOrderId());

    }

    @Override
    public void paymentCanceled(PaymentResponse paymentResponse) {
        orderPaymentSaga.rollback(paymentResponse);
        log.info("Order is Rolling back with failure messages: {} ",
                String.join(FAILURE_MESSAGES_DELIMITER,
                        paymentResponse.getFailureMessages()));

    }
}
