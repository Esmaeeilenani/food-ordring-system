package com.food.ordring.system.outbox;

public interface OutboxScheduler {
    void processesOutboxMessage();
}
