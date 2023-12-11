package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    private final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInformation(order, restaurant);
        order.validateOrder();
        order.initOrder();
        log.info("order with id: {} is initiated", order.getId());
        return new OrderCreatedEvent(order, now());
    }


    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("order with id: {} is paid", order.getId());
        return new OrderPaidEvent(order, now());
    }

    @Override
    public void approveOrder(Order order) {
        order.approve();
        log.info("Order with id : {} is approved", order.getId());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("cancelling Order with id : {}", order.getId());
        return new OrderCancelledEvent(order, now());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessages) {
        order.cancel(failureMessages);
        log.info("Order with id : {} is canceled", order.getId());
    }

    private void setOrderProductInformation(Order order, Restaurant restaurant) {

        order.getOrderItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentOrderProduct = orderItem.getProduct();
            if (currentOrderProduct.equals(restaurantProduct)) {
                currentOrderProduct.updateFiled(restaurantProduct);
            }
        }));
    }

    private void validateRestaurant(Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("restaurant with id: " + restaurant.getId() +
                    " is not Active");
        }
    }

    private ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.of(UTC));
    }
}
