package com.food.ordring.system.order.service.domain.ports.output.repository;

import com.food.ordring.system.order.service.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Optional<Customer> findCustomerById(UUID customerId);

}
