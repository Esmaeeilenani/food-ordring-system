package com.food.ordring.system.order.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordring.system.domain.helper.DateAndTimeUtil;
import com.food.ordring.system.domain.valueobject.*;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordring.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordring.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordring.system.order.service.domain.dto.create.OrderItem;
import com.food.ordring.system.order.service.domain.entity.Customer;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.entity.Product;
import com.food.ordring.system.order.service.domain.entity.Restaurant;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordring.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.PaymentOutboxRepository;
import com.food.ordring.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordring.system.outbox.OutboxStatus;
import com.food.ordring.system.saga.SagaStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfig.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentOutboxRepository paymentOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;


    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3451");
    private final UUID RESTAURANT_ID = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3452");
    private final UUID PRODUCT_ID = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3453");
    private final UUID ORDER_ID = UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454");
    private final UUID SAGA_ID = UUID.fromString("7695069e-7d8d-4138-be9b-969980348e2f");

    private final BigDecimal PRICE = new BigDecimal("200.0");


    @BeforeAll
    public void init() {
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(PRICE)
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();
        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("250.00"))
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1")
                        .postalCode("1000AB")
                        .city("Paris")
                        .build())
                .price(new BigDecimal("210.00"))
                .items(List.of(
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()
                ))
                .build();


        Customer customer = new Customer(new CustomerId(CUSTOMER_ID));


        Restaurant restaurantRes = Restaurant.builder()
                .id(RESTAURANT_ID)
                .products(List.of(
                        new Product(PRODUCT_ID, "product-1", new Money(new BigDecimal("50.0"))),
                        new Product(PRODUCT_ID, "product-2", new Money(new BigDecimal("50.0")))
                ))
                .active(true)
                .build();


        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));



        when(customerRepository.findCustomerById(CUSTOMER_ID))
                .thenReturn(Optional.of(customer));

        when(restaurantRepository.findRestaurantInfo(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantRes));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class)))
                .thenReturn(getOrderPaymentOutboxMessage());
    }



    @Test
    void createOrderCommand_Processed_CreateOrderInPendingStatus() {
        // given precondition or setup

        // when - action or the behaviour that we are going test
        CreateOrderResponse orderResponse = orderApplicationService.createOrder(createOrderCommand);

        // then verify the output
        assertEquals(orderResponse.getOrderStatus(), OrderStatus.PENDING);
        assertEquals(orderResponse.getMessage(), "Order Created Successfully");
        assertNotNull(orderResponse.getOrderTrackingId());

    }

    @Test
    void createOrderCommandWornPrice_Processed_ThrowError() {
        // given precondition or setup

        // when - action or the behaviour that we are going test


        // then verify the output
        assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

    }

    @Test
    void createOrderCommandWornProductPrice_Processed_ThrowError() {
        // given precondition or setup

        // when - action or the behaviour that we are going test


        // then verify the output
        assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));

    }

    @Test
    void createOrderCommandForNotActiveRestaurant_Processed_ThrowError() {
        // given precondition or setup
        Restaurant notActiveRestaurantRes = Restaurant.builder()
                .id(RESTAURANT_ID)
                .products(List.of(
                        new Product(PRODUCT_ID, "product-1", new Money(new BigDecimal("50.0"))),
                        new Product(PRODUCT_ID, "product-2", new Money(new BigDecimal("50.0")))
                ))
                .active(false)
                .build();

        // when - action or the behaviour that we are going test
        when(restaurantRepository.findRestaurantInfo(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(notActiveRestaurantRes));


        // then verify the output
        assertThrows(OrderDomainException.class, () -> orderApplicationService.createOrder(createOrderCommand));

    }


    private OrderPaymentOutboxMessage getOrderPaymentOutboxMessage() {
        OrderPaymentEventPayload orderPaymentEventPayload = OrderPaymentEventPayload
                .builder()
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .createdAt(DateAndTimeUtil.zonedDateTimeUTCNow())
                .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                .build();

        return OrderPaymentOutboxMessage
                .builder()
                .id(UUID.randomUUID())
                .sagaId(SAGA_ID)
                .createdAt(DateAndTimeUtil.zonedDateTimeUTCNow())
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(OrderStatus.PENDING)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();

    }

    private String createPayload(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        }catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create "+o.getClass()+" JSON Object!");
        }
    }

}
