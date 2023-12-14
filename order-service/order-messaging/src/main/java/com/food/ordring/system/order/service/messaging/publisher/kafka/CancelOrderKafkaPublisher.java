package com.food.ordring.system.order.service.messaging.publisher.kafka;

import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.order.service.domain.config.OrderServiceConfig;
import com.food.ordring.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordring.system.order.service.domain.ports.output.message.publisher.payment.OrderCancelledPublisher;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CancelOrderKafkaPublisher implements OrderCancelledPublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfig orderServiceConfig;
    private final KafkaProducer<String, PaymentRequestAvroModel> kafkaProducer;

    private final OrderKafkaPublisherHelper orderKafkaPublisherHelper;

    @Override

    public void publish(OrderCancelledEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCancelledEvent for order : {}", orderId);
        PaymentRequestAvroModel paymentRequestAvroModel = null;
        try {
            paymentRequestAvroModel = orderMessagingDataMapper
                    .orderCancelledEventToPaymentRequestAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfig.getPaymentRequestTopicName(),
                    orderId,
                    paymentRequestAvroModel,
                    orderKafkaPublisherHelper.getKafkaCallback(orderServiceConfig.getPaymentResponseTopicName(),
                            paymentRequestAvroModel,
                            orderId)
            );
            log.info("paymentRequestAvroModel sent to kafka for order id: {}", paymentRequestAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending paymentRequestAvroModel message " +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());

        }
    }


}
