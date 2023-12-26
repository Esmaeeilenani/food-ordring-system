package com.food.ordring.system.payment.service.domain;

import com.food.ordring.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordring.system.payment.service.domain.entity.CreditEntry;
import com.food.ordring.system.payment.service.domain.entity.CreditHistory;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import com.food.ordring.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitPayment(Payment payment,
                                        CreditEntry creditEntry,
                                        List<CreditHistory> creditHistories,
                                        List<String> failureMessages,
                                        DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventPublisher,
                                        DomainEventPublisher<PaymentFailedEvent> PaymentFailedEventPublisher);


    PaymentEvent validateAndCancelPayment(Payment payment,
                                        CreditEntry creditEntry,
                                        List<CreditHistory> creditHistories,
                                        List<String> failureMessages,
                                          DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventPublisher,
                                          DomainEventPublisher<PaymentFailedEvent> PaymentFailedEventPublisher
                                          );



}
