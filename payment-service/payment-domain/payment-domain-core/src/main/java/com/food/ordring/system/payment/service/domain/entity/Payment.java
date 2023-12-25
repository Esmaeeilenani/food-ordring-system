package com.food.ordring.system.payment.service.domain.entity;

import com.food.ordring.system.domain.entity.AggregateRoot;
import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.PaymentStatus;
import com.food.ordring.system.payment.service.domain.valueobject.PaymentId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.plaf.PanelUI;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Payment extends AggregateRoot<PaymentId> {
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;

    private PaymentStatus paymentStatus;

    private ZonedDateTime createdAt;

    public Payment(PaymentId id, OrderId orderId, CustomerId customerId, Money price, PaymentStatus paymentStatus, ZonedDateTime createdAt) {
        super.setId(id);
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public void initPayment() {
        setId(new PaymentId(UUID.randomUUID()));
        createdAt = DateAndTimeUtil.zonedDateTimeUTCNow();

    }

    public void validatePayment(List<String> failureMessages){
        failureMessages = failureMessages == null? new ArrayList<>():failureMessages;

        if (this.price == null || !this.price.isGreaterThanZero()){
            failureMessages.add("Total price must be greater than zero");
        }

    }

    public void updateStatus(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }


    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }


    public static class PaymentBuilder {

        private PaymentId id;

        private OrderId orderId;
        private CustomerId customerId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        PaymentBuilder() {
        }


        public PaymentBuilder id(UUID uuid) {
            this.id = new PaymentId(uuid);
            return this;
        }

        public PaymentBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public PaymentBuilder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public PaymentBuilder price(Money price) {
            this.price = price;
            return this;
        }

        public PaymentBuilder paymentStatus(PaymentStatus paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public PaymentBuilder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Payment build() {
            return new Payment(id, orderId, customerId, price, paymentStatus, createdAt);
        }

        public String toString() {
            return "Payment.PaymentBuilder(orderId=" + this.orderId + ", customerId=" + this.customerId + ", price=" + this.price + ", paymentStatus=" + this.paymentStatus + ", createdAt=" + this.createdAt + ")";
        }
    }
}
