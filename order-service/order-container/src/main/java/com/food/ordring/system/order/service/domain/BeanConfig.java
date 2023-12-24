package com.food.ordring.system.order.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public OrderDomainService orderDomainService(){
        return new OrderDomainServiceImpl();
    }


}
