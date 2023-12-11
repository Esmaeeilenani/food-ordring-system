package com.food.ordring.system.domain.event.publisher;

import com.food.ordring.system.domain.event.DomainEvent;


public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T domianEvent);


}
