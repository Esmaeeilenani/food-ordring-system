package com.food.ordring.system.payment.service.domain;

import com.food.ordring.system.domain.valueobject.PaymentStatus;
import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordring.system.payment.service.domain.entity.CreditEntry;
import com.food.ordring.system.payment.service.domain.entity.CreditHistory;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.exception.PaymentApplicationServiceException;
import com.food.ordring.system.payment.service.domain.mapper.PaymentDataMapper;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentCancelledPublisher;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentCompletedPublisher;
import com.food.ordring.system.payment.service.domain.ports.output.publisher.PaymentFailedPublisher;
import com.food.ordring.system.payment.service.domain.ports.output.repository.CreditEntryRepository;
import com.food.ordring.system.payment.service.domain.ports.output.repository.CreditHistoryRepository;
import com.food.ordring.system.payment.service.domain.ports.output.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentRequestHelper {
    private final PaymentDomainService paymentDomainService;

    private final PaymentDataMapper paymentDataMapper;

    private final PaymentRepository paymentRepository;

    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentCompletedPublisher paymentCompletedPublisher;
    private final PaymentCancelledPublisher paymentCancelledPublisher;
    private final PaymentFailedPublisher paymentFailedPublisher;

    @Transactional
    public PaymentEvent persistCompletePayment(PaymentRequest paymentRequest) {
        return persistPayment(paymentRequest, PaymentStatus.COMPLETED);
    }

    @Transactional
    public PaymentEvent persistCancelledPayment(PaymentRequest paymentRequest) {
        return persistPayment(paymentRequest, PaymentStatus.CANCELLED);
    }

    private PaymentEvent persistPayment(PaymentRequest paymentRequest, PaymentStatus paymentStatus) {
        Payment payment = null;

        if (PaymentStatus.COMPLETED.equals(paymentStatus)) {
            log.info("Received payment complete event for order id: {} ", paymentRequest.getOrderId());
            payment = paymentDataMapper.paymentRequestToPayment(paymentRequest);
        } else if (PaymentStatus.CANCELLED.equals(paymentStatus)) {
            log.info("Received payment cancelled event for order id: {} ", paymentRequest.getOrderId());
            payment = paymentRepository.findByOrderId(UUID.fromString(paymentRequest.getOrderId()))
                    .orElseThrow(() -> new PaymentApplicationServiceException("Payment with order id: " +
                            paymentRequest.getOrderId() +
                            " could not be found"));
        } else {
            return null;
        }

        UUID customerId = payment.getCustomerId().getValue();
        CreditEntry creditEntry = creditEntryRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new PaymentApplicationServiceException("Could not find credit entry for customer id :" + customerId));
        List<CreditHistory> creditHistories = creditHistoryRepository.findAllByCustomerId(customerId);

        List<String> failureMessages = new ArrayList<>();

        PaymentEvent paymentEvent = PaymentStatus.COMPLETED.equals(paymentStatus) ? paymentDomainService.validateAndInitPayment(payment, creditEntry, creditHistories, failureMessages, paymentCompletedPublisher, paymentFailedPublisher) :
                paymentDomainService.validateAndCancelPayment(payment, creditEntry, creditHistories, failureMessages, paymentCancelledPublisher, paymentFailedPublisher);

        paymentRepository.save(payment);
        if (failureMessages.isEmpty()) {
            creditEntryRepository.save(creditEntry);
            creditHistoryRepository.save(creditHistories.get(creditHistories.size() - 1));
        }

        return paymentEvent;
    }


}
