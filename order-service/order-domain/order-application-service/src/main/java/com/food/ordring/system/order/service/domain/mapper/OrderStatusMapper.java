package com.food.ordring.system.order.service.domain.mapper;

import com.food.ordring.system.domain.valueobject.OrderStatus;
import com.food.ordring.system.saga.SagaStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusMapper {
    public SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PAID -> SagaStatus.PROCESSING;
            case APPROVED -> SagaStatus.SUCCEEDED;
            case CANCELLING -> SagaStatus.COMPENSATING;
            case CANCELLED -> SagaStatus.COMPENSATED;
            default -> SagaStatus.STARTED;
        };

    }

}
