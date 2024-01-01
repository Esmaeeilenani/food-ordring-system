package com.food.ordring.system.restaurant.service.domain.entity;

import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderDetail extends BaseEntity<OrderId> {
    private OrderStatus orderStatus;
    private Money totalAmount;
    private final List<Product> products;

    OrderDetail(OrderId orderId, OrderStatus orderStatus, Money totalAmount, List<Product> products) {
        setId(orderId);
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.products = products;
    }

    public static OrderDetailBuilder builder() {
        return new OrderDetailBuilder();
    }


    public static class OrderDetailBuilder {
        private OrderId orderId;

        private OrderStatus orderStatus;
        private Money totalAmount;
        private List<Product> products;

        OrderDetailBuilder() {
        }

        public OrderDetailBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }


        public OrderDetailBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }


        public OrderDetailBuilder totalAmount(Money totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public OrderDetailBuilder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public OrderDetail build() {
            return new OrderDetail(orderId, orderStatus, totalAmount, products);
        }

        public String toString() {
            return "OrderDetail.OrderDetailBuilder(orderStatus=" + this.orderStatus + ", totalAmount=" + this.totalAmount + ", products=" + this.products + ")";
        }
    }
}
