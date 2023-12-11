package com.food.ordring.system.order.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.order.service.domain.valueobject.OrderItemId;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class OrderItem extends BaseEntity<OrderItemId> {
    private OrderId orderId;

    private final Product product;

    private final int quantity;
    private final Money price;
    private final Money subTotal;

    OrderItem(OrderItemId orderItemId, OrderId orderId, Product product, int quantity, Money price, Money subTotal) {
        super.setId(orderItemId);
        this.orderId = orderId;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.subTotal = subTotal;
    }


     void initOrderItem(OrderId orderId, long orderItemId) {
        this.orderId = orderId;
        this.setId(new OrderItemId(orderItemId));
    }

    boolean isPriceValid(){
        return this.price.isGreaterThanZero() &&
                this.price.equals(product.getPrice()) &&
                this.price.multiply(new BigDecimal(quantity)).equals(subTotal);
    }

    public static OrderItemBuilder builder() {
        return new OrderItemBuilder();
    }




    public static class OrderItemBuilder {

        private OrderItemId orderItemId;
        private OrderId orderId;
        private Product product;
        private int quantity;
        private Money price;
        private Money subTotal;

        OrderItemBuilder() {
        }

        public OrderItemBuilder orderItemId(OrderItemId orderItemId) {
            this.orderItemId = orderItemId;
            return this;
        }

        public OrderItemBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderItemBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public OrderItemBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderItemBuilder price(Money price) {
            this.price = price;
            return this;
        }

        public OrderItemBuilder subTotal(Money subTotal) {
            this.subTotal = subTotal;
            return this;
        }

        public OrderItem build() {
            return new OrderItem(orderItemId, orderId, product, quantity, price, subTotal);
        }

        public String toString() {
            return "OrderItem.OrderItemBuilder(orderId=" + this.orderId + ", product=" + this.product + ", quantity=" + this.quantity + ", price=" + this.price + ", subTotal=" + this.subTotal + ")";
        }
    }
}
