package com.food.ordring.system.domain.event;

public final class EmptyEvent implements DomainEvent<Void> {

    public static EmptyEvent INSTANCE = new EmptyEvent();

    private  EmptyEvent(){

    }



}
