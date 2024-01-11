package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.domain.exception.DomainException;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.entity.Customer;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
//this class was created so @Transactional can work
public class CreateOrderHelper {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;

    private final OrderDataMapper orderDataMapper;


    @Transactional
    public OrderCreatedEvent persistOrder(CreateOrderCommand createOrderCommand) {
        checkCustomer(createOrderCommand.getCustomerId());
        Restaurant restaurant = checkRestaurant(createOrderCommand);
        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);

        OrderCreatedEvent orderCreatedEvent = orderDomainService.validateAndInitOrder(order, restaurant);
        Order savedOrder = saveOrder(order);
        log.info("order is created with id: {}", savedOrder.getId());

        return orderCreatedEvent;

    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customerOpt = customerRepository.findCustomerById(customerId);
        if (customerOpt.isEmpty()) {
            log.warn("Couldn't find customer with id : {}", customerId.toString());
            throw new OrderDomainException("Couldn't find customer with id : " + customerId.toString());
        }


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

    private Order saveOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        if (savedOrder == null) {
            throw new OrderDomainException("Could not save order");
        }

        log.info("order is saved with id : {}", savedOrder.getId());
        return savedOrder;
    }


}
