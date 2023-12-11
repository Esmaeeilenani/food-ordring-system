package com.food.ordring.system.order.service.domain.entity;

import com.food.ordring.system.domain.entity.AggregateRoot;
import com.food.ordring.system.domain.valueobject.*;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.valueobject.StreetAddress;
import com.food.ordring.system.order.service.domain.valueobject.TrackingId;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Getter
public class Order extends AggregateRoot<OrderId> {
    private final CustomerId customerId;
    private final RestaurantId restaurantId;
    private final StreetAddress deliveryAddress;
    private final Money price;
    private final List<OrderItem> orderItems;
    private TrackingId trackingId;

    private OrderStatus orderStatus;

    private List<String> failureMessages;

    Order(OrderId orderId, CustomerId customerId, RestaurantId restaurantId, StreetAddress deliveryAddress, Money price, List<OrderItem> orderItems, TrackingId trackingId, OrderStatus orderStatus, List<String> failureMessages) {
        super.setId(orderId);
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.deliveryAddress = deliveryAddress;
        this.price = price;
        this.orderItems = orderItems;
        this.trackingId = trackingId;
        this.orderStatus = orderStatus;
        this.failureMessages = failureMessages;
    }

    @Override
    public OrderId getId() {
        return super.getId();
    }

    public void initOrder() {
        setId(new OrderId(UUID.randomUUID()));
        this.trackingId = new TrackingId(UUID.randomUUID());
        this.orderStatus = OrderStatus.PENDING;
        initOrderItems();
    }

    public void validateOrder() {
        validateInitOrder();
        validateTotalPrice();
        validateItemPrice();
    }

    public void pay() {
        if (!this.orderStatus.equals(OrderStatus.PENDING)) {
            throw new OrderDomainException("order is not in correct state for payment");
        }
        this.orderStatus = OrderStatus.PAID;

    }

    public void approve() {
        if (!this.orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("order is not in correct state to be approved");
        }
        this.orderStatus = OrderStatus.APPROVED;

    }

    public void initCancel(List<String> failureMessages) {
        if (!this.orderStatus.equals(OrderStatus.PAID)) {
            throw new OrderDomainException("order is not in correct state for InitCancel");
        }

        this.orderStatus = OrderStatus.CANCELLING;
        updateFailureMessages(failureMessages);
    }

    private void updateFailureMessages(List<String> failureMessages) {
        if (failureMessages == null) {
            return;
        }
        failureMessages = failureMessages.stream()
                .filter(Objects::nonNull)
                .filter(m -> !m.isBlank())
                .map(String::trim)
                .toList();

        if (this.failureMessages == null) {
            this.failureMessages = new ArrayList<>();
        }
        this.failureMessages.addAll(failureMessages.stream()
                .filter(Objects::nonNull)
                .filter(m -> !m.isBlank())
                .toList()
        );
    }

    public void cancel(List<String> failureMessages) {
        if (!(this.orderStatus.equals(OrderStatus.CANCELLING) || this.orderStatus.equals(OrderStatus.PENDING))) {
            throw new OrderDomainException("order is not in correct state to be Cancelled");
        }
        this.orderStatus = OrderStatus.CANCELLED;
        updateFailureMessages(failureMessages);
    }

    private void validateInitOrder() {
        if (this.orderStatus != null || super.getId() != null) {
            throw new OrderDomainException("Order is not in correct status or has been initialized");
        }
    }

    private void validateTotalPrice() {
        if (this.price == null || !this.price.isGreaterThanZero()) {
            throw new OrderDomainException("Total price must be greater than zero");
        }
    }

    private void validateItemPrice() {
        Money orderItemsTotal = this.orderItems
                .stream()
                .map(orderItem -> {
                    validateItemPrice(orderItem);
                    return orderItem.getSubTotal();
                }).reduce(Money.ZERO, Money::add);

        if (!this.price.equals(orderItemsTotal)) {
            throw new OrderDomainException("Total price: " + price.getAmount()
                    + " is not equal to order items total: " + orderItemsTotal.getAmount());
        }
    }

    private void validateItemPrice(OrderItem orderItem) {
        if (!orderItem.isPriceValid()) {
            throw new OrderDomainException("order item price: " + orderItem.getPrice().getAmount()
                    + " is not valid for product : " + orderItem.getProduct().getId());
        }
    }

    private void initOrderItems() {
        long itemId = 1;
        for (OrderItem orderItem : this.orderItems) {
            orderItem.initOrderItem(this.getId(), itemId);
            itemId++;
        }


    }


    public static OrderBuilder builder() {
        return new OrderBuilder();
    }


    public static class OrderBuilder {

        private OrderId orderId;

        private CustomerId customerId;
        private RestaurantId restaurantId;
        private StreetAddress deliveryAddress;
        private Money price;
        private List<OrderItem> orderItems;
        private TrackingId trackingId;
        private OrderStatus orderStatus;
        private List<String> failureMessages;

        OrderBuilder() {
        }

        public OrderBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder customerId(CustomerId customerId) {
            this.customerId = customerId;
            return this;
        }

        public OrderBuilder restaurantId(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public OrderBuilder deliveryAddress(StreetAddress deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
            return this;
        }

        public OrderBuilder price(Money price) {
            this.price = price;
            return this;
        }

        public OrderBuilder orderItems(List<OrderItem> orderItems) {
            this.orderItems = orderItems;
            return this;
        }

        public OrderBuilder trackingId(TrackingId trackingId) {
            this.trackingId = trackingId;
            return this;
        }

        public OrderBuilder orderStatus(OrderStatus orderStatus) {
            this.orderStatus = orderStatus;
            return this;
        }

        public OrderBuilder failureMessages(List<String> failureMessages) {
            this.failureMessages = failureMessages;
            return this;
        }

        public Order build() {
            return new Order(orderId, customerId, restaurantId, deliveryAddress, price, orderItems, trackingId, orderStatus, failureMessages);
        }

        public String toString() {
            return "Order.OrderBuilder(customerId=" + this.customerId + ", restaurantId=" + this.restaurantId + ", deliveryAddress=" + this.deliveryAddress + ", price=" + this.price + ", orderItems=" + this.orderItems + ", trackingId=" + this.trackingId + ", orderStatus=" + this.orderStatus + ", failureMessages=" + this.failureMessages + ")";
        }
    }
}
