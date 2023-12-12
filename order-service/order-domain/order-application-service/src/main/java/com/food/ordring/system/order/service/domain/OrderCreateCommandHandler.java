package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.exception.DomainException;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.entity.Customer;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.OrderCreatedPublisher;
import com.food.ordring.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCreateCommandHandler {

    private final OrderDomainService orderDomainService;

    private final OrderRepository orderRepository;

    private final CustomerRepository customerRepository;

    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;

    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

//    private final OrderCreatedPublisher orderCreatedPublisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);

        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitOrder(order, restaurant);
        Order savedOrder = saveOrder(order);
        log.info("order is created with id: {}", savedOrder.getId());

//        orderCreatedPublisher.publish(orderCreatedEvent);
        applicationDomainEventPublisher.publish(orderCreatedEvent);
        return orderDataMapper.orderToCreateOrderResponse(savedOrder);
    }


    private Restaurant checkRestaurant(CreateOrderCommand createOrderCommand) {
        Restaurant restaurant = orderDataMapper.createOrderCommandToRestaurant(createOrderCommand);
        Optional<Restaurant> restaurantInfo = restaurantRepository.findRestaurantInfo(restaurant);

        if (restaurantInfo.isEmpty()) {
            log.warn("couldn't find restaurant with id: {} ", createOrderCommand.getRestaurantId());
            throw new DomainException("couldn't find restaurant with id: " + createOrderCommand.getRestaurantId());
        }

        return restaurantInfo.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerOpt = customerRepository.findCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            log.warn("Couldn't find customer with id : {}", customerId.toString());
            throw new OrderDomainException("Couldn't find customer with id : " + customerId.toString());
        }


    }

    private Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new OrderDomainException("Could not save order");
        }

        log.info("order is saved with id : {}", savedOrder.getId());
        return savedOrder;
    }
}
