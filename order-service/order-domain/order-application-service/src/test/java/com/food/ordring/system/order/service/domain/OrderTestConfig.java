package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.food.ordring.system")
public class OrderTestConfig {



    @Bean
    public OrderRepository orderRepository(){
     return Mockito.mock(OrderRepository.class);
    }

    @Bean
    public CustomerRepository customerRepository(){
     return Mockito.mock(CustomerRepository.class);
    }

    @Bean
    public RestaurantRepository restaurantRepository(){
     return Mockito.mock(RestaurantRepository.class);
    }

    @Bean
    public OrderDomainService orderDomainService(){
     return new OrderDomainServiceImpl();
    }



}
