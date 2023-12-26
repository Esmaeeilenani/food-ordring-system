package com.food.ordring.system.domain.event;

public interface DomainEvent<EventCreator> {
    void fire(); 
}
