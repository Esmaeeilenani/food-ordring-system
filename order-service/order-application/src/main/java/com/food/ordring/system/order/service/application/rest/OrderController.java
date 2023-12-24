package com.food.ordring.system.order.service.application.rest;

import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordring.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordring.system.order.service.domain.ports.input.service.OrderApplicationService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {
    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody @Valid CreateOrderCommand createOrderCommand) {
        log.info("Creating Order from customer: {} at restaurant: {}", createOrderCommand.getCustomerId(), createOrderCommand.getRestaurantId());


        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("order created with tracking id: {}", orderResponse.getOrderTrackingId());

        return ResponseEntity.ok(orderResponse);

    }


    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderTracking(@PathVariable("trackingId") UUID trackingId) {
        TrackOrderResponse trackOrderResponse = orderApplicationService.trackOrder(TrackOrderQuery.builder()
                .orderTrackingId(trackingId).build());
        log.info("returning order status with id: {}", trackingId);
        return ResponseEntity.ok(trackOrderResponse);
    }


}
