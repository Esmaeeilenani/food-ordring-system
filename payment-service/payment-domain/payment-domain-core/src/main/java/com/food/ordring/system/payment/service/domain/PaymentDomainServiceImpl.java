package com.food.ordring.system.payment.service.domain;

import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.PaymentStatus;
import com.food.ordring.system.payment.service.domain.entity.CreditEntry;
import com.food.ordring.system.payment.service.domain.entity.CreditHistory;
import com.food.ordring.system.payment.service.domain.entity.Payment;
import com.food.ordring.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordring.system.payment.service.domain.valueobject.TransactionType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.food.ordring.system.domain.helper.DateAndTimeUtil.zonedDateTimeUTCNow;

@Slf4j
public class PaymentDomainServiceImpl implements PaymentDomainService {
    @Override
    public PaymentEvent validateAndInitPayment(Payment payment,
                                               CreditEntry creditEntry,
                                               List<CreditHistory> creditHistories,
                                               List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        payment.initPayment();
        validateCreditEntry(payment, creditEntry, failureMessages);
        subtractCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.DEBIT);
        validateCreditHistory(creditEntry, creditHistories, failureMessages);

        if (!failureMessages.isEmpty()) {
            log.info("Payment is failed for order id: {}", payment.getOrderId());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment, zonedDateTimeUTCNow(), failureMessages);
        }

        log.info("Payment is initialized for order id: {}", payment.getOrderId());
        payment.updateStatus(PaymentStatus.COMPLETED);
        return new PaymentCompletedEvent(payment, zonedDateTimeUTCNow());


    }


    @Override
    public PaymentEvent validateAndCancelPayment(Payment payment, CreditEntry creditEntry, List<CreditHistory> creditHistories, List<String> failureMessages) {
        payment.validatePayment(failureMessages);
        addCreditEntry(payment, creditEntry);
        updateCreditHistory(payment, creditHistories, TransactionType.CREDIT);

        if (!failureMessages.isEmpty()) {
            log.error("Payment cancellation is failed for order id : {}",payment.getOrderId());
            payment.updateStatus(PaymentStatus.FAILED);
            return new PaymentFailedEvent(payment,zonedDateTimeUTCNow(),failureMessages);
        }

        log.info("Payment is successfully cancelled for order id : {}",payment.getOrderId());
        payment.updateStatus(PaymentStatus.CANCELLED);
        return new PaymentCancelledEvent(payment, zonedDateTimeUTCNow());
    }


    private void validateCreditEntry(Payment payment, CreditEntry creditEntry, List<String> failureMessages) {
        if (payment.getPrice().isGreaterThan(creditEntry.getTotalCreditAmount())) {
            log.error("Customer with id: {} doesn't have enough credit for payment", payment.getCustomerId());

            failureMessages.add("Customer with id: " +
                    payment.getCustomerId()
                    + " doesn't have enough credit for payment");
        }
    }

    private void subtractCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.subtractCreditAmount(payment.getPrice());
    }

    private void updateCreditHistory(Payment payment,
                                     List<CreditHistory> creditHistories,
                                     TransactionType transactionType) {
        creditHistories.add(
                CreditHistory.builder()
                        .id(UUID.randomUUID())
                        .customerId(payment.getCustomerId())
                        .amount(payment.getPrice())
                        .transactionType(transactionType)
                        .build()

        );
    }


    private void validateCreditHistory(CreditEntry creditEntry,
                                       List<CreditHistory> creditHistories,
                                       List<String> failureMessages) {

        Money totalCreditHistory = getHistoryTotalAmount(creditHistories, TransactionType.CREDIT);

        Money totalDebitHistory = getHistoryTotalAmount(creditHistories, TransactionType.DEBIT);

        if (totalDebitHistory.isGreaterThan(totalCreditHistory)) {
            log.error("Customer with id: {} doesn't have enough credit", creditEntry.getCustomerId());
            failureMessages.add("Customer with id: " + creditEntry.getCustomerId() + " doesn't have enough credit");

        }

        if (!creditEntry.getTotalCreditAmount().equals(totalCreditHistory.subtract(totalDebitHistory))) {
            log.error("Credit history total is not equal to current credit for customer with id: {}", creditEntry.getCustomerId());
            failureMessages.add("Credit history total is not equal to current credit for customer with id: " + creditEntry.getCustomerId());
        }

    }


    private Money getHistoryTotalAmount(List<CreditHistory> creditHistories, TransactionType transactionType) {
        if (creditHistories == null || creditHistories.isEmpty()) {
            return Money.ZERO;
        }


        return creditHistories
                .stream()
                .filter(creditHistory -> creditHistory.getTransactionType().equals(transactionType))
                .map(CreditHistory::getAmount)
                .reduce(Money.ZERO, Money::add);


    }

    private void addCreditEntry(Payment payment, CreditEntry creditEntry) {
        creditEntry.addCreditAmount(payment.getPrice());
    }


}
