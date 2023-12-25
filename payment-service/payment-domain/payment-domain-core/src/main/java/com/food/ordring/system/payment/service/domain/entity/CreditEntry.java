package com.food.ordring.system.payment.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.CustomerId;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.payment.service.domain.valueobject.CreditEntryId;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreditEntry extends BaseEntity<CreditEntryId> {

    private final CustomerId customerId;
    private Money totalCreditAmount;

    public void addCreditAmount(Money amount){
        this.totalCreditAmount = this.totalCreditAmount.add(amount);
    }


    public void subtractCreditAmount(Money amount){
        this.totalCreditAmount = this.totalCreditAmount.subtract(amount);
    }


    public CreditEntry(CreditEntryId creditEntryId,CustomerId customerId, Money totalCreditAmount) {
        setId(creditEntryId);
        this.customerId = customerId;
        this.totalCreditAmount = totalCreditAmount;
    }

    public static CreditEntryBuilder builder() {
        return new CreditEntryBuilder();
    }


    public static class CreditEntryBuilder {

        private CreditEntryId id;

        private CustomerId customerId;
        private Money totalCreditAmount;

        CreditEntryBuilder() {
        }

        public CreditEntryBuilder id(UUID uuid) {
            this.id = new CreditEntryId(uuid);
            return this;
        }

        public CreditEntryBuilder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public CreditEntryBuilder totalCreditAmount(Money totalCreditAmount) {
            this.totalCreditAmount = totalCreditAmount;
            return this;
        }

        public CreditEntry build() {
            return new CreditEntry(id,customerId, totalCreditAmount);
        }

        public String toString() {
            return "CreditEntry.CreditEntryBuilder(customerId=" + this.customerId + ", totalCreditAmount=" + this.totalCreditAmount + ")";
        }
    }
}
