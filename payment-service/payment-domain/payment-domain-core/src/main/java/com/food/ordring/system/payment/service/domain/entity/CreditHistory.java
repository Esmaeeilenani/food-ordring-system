package com.food.ordring.system.payment.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.payment.service.domain.valueobject.CreditHistoryId;
import com.food.ordring.system.payment.service.domain.valueobject.TransactionType;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreditHistory extends BaseEntity<CreditHistoryId> {
    private final CustomerId customerId;
    private final Money amount;
    private final TransactionType transactionType;

    public CreditHistory(CreditHistoryId creditHistoryId, CustomerId customerId, Money amount, TransactionType transactionType) {
        setId(new CreditHistoryId(UUID.randomUUID()));
        this.customerId = customerId;
        this.amount = amount;
        this.transactionType = transactionType;
    }

    public static CreditHistoryBuilder builder() {
        return new CreditHistoryBuilder();
    }


    public static class CreditHistoryBuilder {
        private CreditHistoryId id;
        private CustomerId customerId;
        private Money amount;
        private TransactionType transactionType;

        CreditHistoryBuilder() {
        }

        public CreditHistoryBuilder id(UUID uuid) {
            this.id = new CreditHistoryId(uuid);
            return this;
        }

        public CreditHistoryBuilder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public CreditHistoryBuilder amount(Money amount) {
            this.amount = amount;
            return this;
        }

        public CreditHistoryBuilder transactionType(TransactionType transactionType) {
            this.transactionType = transactionType;
            return this;
        }

        public CreditHistory build() {
            return new CreditHistory(id, customerId, amount, transactionType);
        }

        public String toString() {
            return "CreditHistory.CreditHistoryBuilder(customerId=" + this.customerId + ", amount=" + this.amount + ", transactionType=" + this.transactionType + ")";
        }
    }
}
