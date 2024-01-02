package com.food.ordring.system.restaurant.service.domain.exception;

import com.food.ordring.system.domain.exception.DomainException;

public class RestaurantServiceException extends DomainException {
    public RestaurantServiceException(String message) {
        super(message);
    }

    public RestaurantServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
