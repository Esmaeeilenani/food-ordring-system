package com.food.ordring.system.order.service.domain.dto.create;

import com.food.ordring.system.domain.valueobject.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
