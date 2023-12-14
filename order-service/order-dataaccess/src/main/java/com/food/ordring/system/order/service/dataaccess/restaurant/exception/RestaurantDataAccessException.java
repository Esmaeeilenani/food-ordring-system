package com.food.ordring.system.order.service.dataaccess.restaurant.exception;

public class RestaurantDataAccessException extends RuntimeException {
    public RestaurantDataAccessException(String message) {
        super(message);
    }

    public RestaurantDataAccessException(Throwable cause) {
        super(cause);
    }
}
