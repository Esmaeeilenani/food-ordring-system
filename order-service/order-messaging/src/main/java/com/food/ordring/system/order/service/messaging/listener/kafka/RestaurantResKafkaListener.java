package com.food.ordring.system.order.service.messaging.listener.kafka;

import com.food.ordring.system.kafka.consumer.KafkaConsumer;
import com.food.ordring.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordring.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordring.system.order.service.domain.entity.Order;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.restaurant.RestaurantResListener;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class RestaurantResKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

    private final RestaurantResListener restaurantResListener;

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    @KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
            topics = "${order-service.restaurant-approval-response-topic-name}")
    @Override
    public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of restaurant approval response received with keys:{}, partitions:{}, and offsets:{}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(rarAvroModel -> {
            if (OrderApprovalStatus.APPROVED.equals(rarAvroModel.getOrderApprovalStatus())) {
                log.info("Processing approved order for order id:{}",rarAvroModel.getOrderId());
                restaurantResListener.orderApproved(orderMessagingDataMapper.restaurantApprovalAvroToRestaurantApprovalResponse(rarAvroModel));
            }
            if (OrderApprovalStatus.REJECTED.equals(rarAvroModel.getOrderApprovalStatus())) {
                log.info("Processing rejected  order for order id:{}, failure messages: {}",rarAvroModel.getOrderId(),
                        String.join(Order.FAILURE_MESSAGES_DELIMITER, rarAvroModel.getFailureMessages()));
                restaurantResListener.orderRejected(orderMessagingDataMapper.restaurantApprovalAvroToRestaurantApprovalResponse(rarAvroModel));
            }


        });

    }
}
