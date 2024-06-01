package com.food.ordring.system.order.service.messaging.publisher.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordring.system.kafka.order.avro.model.RestaurantApprovalRequestAvroModel;
import com.food.ordring.system.kafka.producer.KafkaMessageHelper;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.order.service.domain.config.OrderServiceConfig;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.approval.OrderApprovalOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.restaurant.RestaurantApprovalPub;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordring.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderApprovalEventPublisher implements RestaurantApprovalPub {


    private final OrderMessagingDataMapper orderMessagingDataMapper;

    private final KafkaProducer<String, RestaurantApprovalRequestAvroModel> kafaKafkaProducer;

    private final OrderServiceConfig orderServiceConfig;

    private final KafkaMessageHelper kafkaMessageHelper;


    @Override
    public void publisher(OrderApprovalOutboxMessage orderApprovalOutboxMessage,
                          BiConsumer<OrderApprovalOutboxMessage, OutboxStatus> outboxCallBack) {

        OrderApprovalEventPayload orderApprovalEventPayload = kafkaMessageHelper.getEventPayload(orderApprovalOutboxMessage.getPayload(),
                OrderApprovalEventPayload.class);
        String sagaId = orderApprovalOutboxMessage.getSagaId().toString();
        String orderId = orderApprovalEventPayload.getOrderId();

        log.info("Received orderApprovalOutboxMessage for order id :{}, and saga id:{}",
                orderId, sagaId);

        try {
            RestaurantApprovalRequestAvroModel approvalRequestAvroModel = orderMessagingDataMapper
                    .orderApprovalEventToApprovalRequestAvroModel(sagaId, orderApprovalEventPayload);
            String approvalRequestTopicName = orderServiceConfig.getRestaurantApprovalRequestTopicName();
            ListenableFutureCallback<SendResult<String, RestaurantApprovalRequestAvroModel>> kafkaCallback = kafkaMessageHelper
                    .getKafkaCallback(approvalRequestTopicName,
                            approvalRequestAvroModel,
                            orderApprovalOutboxMessage,
                            outboxCallBack,
                            orderId);


            kafaKafkaProducer.send(
                    approvalRequestTopicName,
                    sagaId,
                    approvalRequestAvroModel,
                    kafkaCallback
            );

            log.info("orderApprovalEventPayload sent to kafka for order id: {} and saga id : {}",
                    orderId, sagaId);

        } catch (Exception e) {
            log.error("Error while sending orderApprovalEventPayload to kafak with order id:{} and saga id: {} error: {}",
                    orderId, sagaId, e.getMessage());
        }


    }
}
