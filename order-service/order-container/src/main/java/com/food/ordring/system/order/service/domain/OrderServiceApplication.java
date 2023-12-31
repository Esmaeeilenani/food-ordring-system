package com.food.ordring.system.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"com.food.ordring.system.order.service.dataaccess","com.food.ordering.system.dataaccess"})
@EntityScan(basePackages = {"com.food.ordring.system.order.service.dataaccess","com.food.ordering.system.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.food.ordring.system")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class,args);
    }
}
