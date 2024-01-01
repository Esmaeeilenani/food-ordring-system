package com.food.ordring.system.restaurant.service.domain.entity;

import com.food.ordring.system.domain.entity.AggregateRoot;
import com.food.ordring.system.domain.valueobject.Money;
import com.food.ordring.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.restaurant.service.domain.valueobject.OrderApprovalId;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Restaurant extends AggregateRoot<RestaurantId> {

    private OrderApproval orderApproval;
    private boolean active;

    private final OrderDetail orderDetail;

    public void validateOrder(List<String> failureMessages) {
        if (!orderDetail.getOrderStatus().equals(OrderStatus.PAID)) {
            failureMessages.add("Payment is not complete for order: " + orderDetail.getId());
        }
        Money totalAmount = orderDetail.getProducts()
                .stream()
                .map(product -> {
                    if (!product.isAvailable()) {
                        failureMessages.add("product with id: " + product.getId() + " is not available");
                    }
                    return product.getPrice().multiply(new BigDecimal(product.getQuantity()));
                }).reduce(Money.ZERO, Money::add);

        if (!orderDetail.getTotalAmount().equals(totalAmount)) {
            failureMessages.add("price total is not correct for order: " + orderDetail.getId());
        }


    }

    public void constructOrderApproval(OrderApprovalStatus orderApprovalStatus) {
        this.orderApproval = OrderApproval.builder()
                .orderApprovalId(new OrderApprovalId(UUID.randomUUID()))
                .restaurantId(this.getId())
                .orderId(this.orderDetail.getId())
                .orderApprovalStatus(orderApprovalStatus)
                .build();
    }


    Restaurant(RestaurantId restaurantId, OrderApproval orderApproval, boolean active, OrderDetail orderDetail) {
        setId(restaurantId);
        this.orderApproval = orderApproval;
        this.active = active;
        this.orderDetail = orderDetail;
    }

    public static RestaurantBuilder builder() {
        return new RestaurantBuilder();
    }


    public static class RestaurantBuilder {
        private RestaurantId restaurantId;

        private OrderApproval orderApproval;
        private boolean active;
        private OrderDetail orderDetail;

        RestaurantBuilder() {
        }

        public RestaurantBuilder restaurantId(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public RestaurantBuilder orderApproval(OrderApproval orderApproval) {
            this.orderApproval = orderApproval;
            return this;
        }

        public RestaurantBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public RestaurantBuilder orderDetail(OrderDetail orderDetail) {
            this.orderDetail = orderDetail;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(restaurantId, orderApproval, active, orderDetail);
        }

        public String toString() {
            return "Restaurant.RestaurantBuilder(orderApproval=" + this.orderApproval + ", active=" + this.active + ", orderDetail=" + this.orderDetail + ")";
        }
    }
}
