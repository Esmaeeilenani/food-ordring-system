package com.food.ordring.system.order.service.domain.dto.create;

import com.food.ordring.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {

    @NotNull
    private UUID orderTrackingId;

    @NotNull
    private final OrderStatus orderStatus;

    @NotEmpty
    private final String message;

}
