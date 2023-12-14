package com.food.ordring.system.order.service.messaging.publisher.kafka;

import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordring.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.order.service.domain.config.OrderServiceConfig;
import com.food.ordring.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant.OrderPaidPublisher;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class PayOrderKafkaPublisher implements OrderPaidPublisher {
    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfig orderServiceConfig;
    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafkaProducer;
    private final OrderKafkaPublisherHelper orderKafkaPublisherHelper;

    @Override
    public void publish(OrderPaidEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        RestaurantApprovalRequestAvroModel restaurantApprovalRequestAvroModel = orderMessagingDataMapper.OrderPaidEventToRestaurantApprovalRequestAvroModel(domainEvent);

        try {
            kafkaProducer.send(orderServiceConfig.getRestaurantApprovalRequestTopicName(),
                    orderId,
                    restaurantApprovalRequestAvroModel,
                    orderKafkaPublisherHelper
                            .getKafkaCallback(orderServiceConfig.getRestaurantApprovalResponseTopicName(),
                                    restaurantApprovalRequestAvroModel,
                                    orderId
                            )

            );

            log.info("restaurantApprovalRequestAvroModel sent to kafka for order id: {}", orderId);
        } catch (Exception e) {
            log.error("Error while sending restaurantApprovalRequestAvroModel message " +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());

        }


    }
}
