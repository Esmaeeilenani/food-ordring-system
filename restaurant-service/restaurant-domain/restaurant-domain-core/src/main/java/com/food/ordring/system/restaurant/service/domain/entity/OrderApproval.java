package com.food.ordring.system.restaurant.service.domain.entity;


import com.food.ordring.system.domain.entity.BaseEntity;
import com.food.ordring.system.domain.valueobject.OrderApprovalStatus;
import com.food.ordring.system.domain.valueobject.OrderId;
import com.food.ordring.system.domain.valueobject.RestaurantId;
import com.food.ordring.system.restaurant.service.domain.valueobject.OrderApprovalId;
import lombok.Getter;

@Getter
public class OrderApproval extends BaseEntity<OrderApprovalId> {

    private final RestaurantId restaurantId;
    private final OrderId orderId;
    private final OrderApprovalStatus orderApprovalStatus;

    OrderApproval(OrderApprovalId orderApprovalId, RestaurantId restaurantId, OrderId orderId, OrderApprovalStatus orderApprovalStatus) {
        setId(orderApprovalId);
        this.restaurantId = restaurantId;
        this.orderId = orderId;
        this.orderApprovalStatus = orderApprovalStatus;
    }

    public static OrderApprovalBuilder builder() {
        return new OrderApprovalBuilder();
    }


    public static class OrderApprovalBuilder {
        private OrderApprovalId orderApprovalId;
        private RestaurantId restaurantId;
        private OrderId orderId;
        private OrderApprovalStatus orderApprovalStatus;

        OrderApprovalBuilder() {
        }

        public OrderApprovalBuilder orderApprovalId(OrderApprovalId orderApprovalId) {
            this.orderApprovalId = orderApprovalId;
            return this;
        }

        public OrderApprovalBuilder restaurantId(RestaurantId restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }


        public OrderApprovalBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderApprovalBuilder orderApprovalStatus(OrderApprovalStatus orderApprovalStatus) {
            this.orderApprovalStatus = orderApprovalStatus;
            return this;
        }

        public OrderApproval build() {
            return new OrderApproval(orderApprovalId, restaurantId, orderId, orderApprovalStatus);
        }

        public String toString() {
            return "OrderApproval.OrderApprovalBuilder(restaurantId=" + this.restaurantId + ", orderId=" + this.orderId + ", orderApprovalStatus=" + this.orderApprovalStatus + ")";
        }
    }
}
