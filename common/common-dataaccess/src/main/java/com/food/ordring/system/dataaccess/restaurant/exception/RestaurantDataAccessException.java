package com.food.ordring.system.dataaccess.restaurant.exception;

public class RestaurantDataAccessException extends RuntimeException {
    public RestaurantDataAccessException(String message) {
        super(message);
    }

    public RestaurantDataAccessException(Throwable cause) {
        super(cause);
    }
}
