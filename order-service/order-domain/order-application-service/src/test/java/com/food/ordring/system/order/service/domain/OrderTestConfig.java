package com.food.ordring.system.order.service.domain;

import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestPublisher;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalPub;
import com.food.ordring.system.order.service.domain.ports.output.repository.*;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.food.ordring.system")
public class OrderTestConfig {


    @Bean
    public PaymentRequestPublisher paymentRequestPublisher(){
        return Mockito.mock(PaymentRequestPublisher.class);
    }

    @Bean
    public RestaurantApprovalPub restaurantApprovalPub(){
        return Mockito.mock(RestaurantApprovalPub.class);
    }


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
    public PaymentOutboxRepository paymentOutboxRepository(){
     return Mockito.mock(PaymentOutboxRepository.class);
    }
    @Bean
    public ApprovalOutboxRepository approvalOutboxRepository(){
     return Mockito.mock(ApprovalOutboxRepository.class);
    }



    @Bean
    public OrderDomainService orderDomainService(){
     return new OrderDomainServiceImpl();
    }



}
