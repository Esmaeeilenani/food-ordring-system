package com.food.ordring.system.order.service.messaging.publisher.kafka;

import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordring.system.kafka.producer.KafkaMessageHelper;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.order.service.domain.config.OrderServiceConfig;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordring.system.order.service.domain.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.PaymentRequestPublisher;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.food.ordring.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderPaymentEventPublisher implements PaymentRequestPublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;

    private final KafkaProducer<String, PaymentRequestAvroModel> kafaKafkaProducer;

    private final OrderServiceConfig orderServiceConfig;

    private final KafkaMessageHelper kafkaMessageHelper;



    @Override
    public void publish(OrderPaymentOutboxMessage orderPaymentOutboxMessage,
                        BiConsumer<OrderPaymentOutboxMessage, OutboxStatus> outboxCallBack) {

        OrderPaymentEventPayload orderPaymentEventPayload = kafkaMessageHelper.getEventPayload(orderPaymentOutboxMessage.getPayload(),
                OrderPaymentEventPayload.class);

        String sagaId = orderPaymentOutboxMessage.getSagaId().toString();

        log.info("Received OrderPaymentOutboxMessage for order id {} and saga id {}",
                orderPaymentOutboxMessage.getId().toString(), sagaId);


        try {
            PaymentRequestAvroModel paymentRequestAvroModel = orderMessagingDataMapper
                    .orderPaymentEventToPaymentRequestAvroModel(sagaId, orderPaymentEventPayload);

            String paymentRequestTopicName = orderServiceConfig.getPaymentRequestTopicName();

            ListenableFutureCallback<SendResult<String, PaymentRequestAvroModel>> kafkaCallback = kafkaMessageHelper
                    .getKafkaCallback(paymentRequestTopicName,
                            paymentRequestAvroModel,
                            orderPaymentOutboxMessage,
                            outboxCallBack,
                            orderPaymentEventPayload.getOrderId());

            kafaKafkaProducer.send(paymentRequestTopicName,
                    sagaId,
                    paymentRequestAvroModel,
                    kafkaCallback);
            log.info("orderPaymentEventPayload sent to kafka for order id: {} and saga id : {}",
                    orderPaymentEventPayload.getOrderId(), sagaId);


        } catch (Exception e) {
            log.error("Error while sending orderPaymentEventPayload to kafak with order id:{} and saga id: {} error: {}",
                    orderPaymentEventPayload.getOrderId(), sagaId, e.getMessage());


        }

    }


}
