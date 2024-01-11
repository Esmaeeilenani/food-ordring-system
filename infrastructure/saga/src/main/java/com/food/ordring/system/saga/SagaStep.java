package com.food.ordring.system.saga;

import com.food.ordring.system.domain.event.DomainEvent;

public interface SagaStep<T> {

    void process(T data);

    void rollback(T data);
}
