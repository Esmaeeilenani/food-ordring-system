package com.food.ordring.system.payment.service.messaging.publisher;

import com.food.ordring.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordring.system.kafka.producer.KafkaMessageCallbackHelper;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordring.system.payment.service.domain.event.PaymentCancelledEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentCompletedEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import com.food.ordring.system.payment.service.domain.event.PaymentFailedEvent;
import com.food.ordring.system.payment.service.domain.exception.PaymentDomainException;
import com.food.ordring.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class PaymentEventKafkaPublisher<T extends PaymentEvent> {

    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentServiceConfigData paymentServiceConfigData;

    private final KafkaMessageCallbackHelper kafkaMessageCallbackHelper;

    public void publish(T paymentEvent) {



        String orderId = paymentEvent.getPayment().getOrderId().toString();
        log.info("Received {} for order id: {} ", paymentEvent.getClass().getName(), orderId);

        PaymentResponseAvroModel paymentResponseAvroModel = mapToPaymentResponseAvroModel(paymentEvent);

        String responseTopicName = paymentServiceConfigData.getPaymentResponseTopicName();

        try {
            kafkaProducer.send(responseTopicName,
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageCallbackHelper.getKafkaCallback(
                            responseTopicName,
                            paymentResponseAvroModel,
                            orderId
                    ));

            log.info("{} sent to kafka for order id: {}", paymentResponseAvroModel.getClass().getName(), orderId);

        } catch (Exception e) {
            log.info("Error while sending {} message to kafka with order id: {} error: {}",
                    paymentResponseAvroModel.getClass().getName(),
                    orderId,
                    e.getMessage()
            );
        }


    }


    private PaymentResponseAvroModel mapToPaymentResponseAvroModel(T paymentEvent){
       return switch (paymentEvent){
           case PaymentCompletedEvent event -> paymentMessagingDataMapper.paymentCompletedEventToPaymentResponseAvroModel(event);
           case PaymentCancelledEvent event -> paymentMessagingDataMapper.paymentCancelledEventToPaymentResponseAvroModel(event);
           case PaymentFailedEvent event -> paymentMessagingDataMapper.paymentFailedEventToPaymentResponseAvroModel(event);
           default -> throw new PaymentDomainException("event not supported");
       };
    }

}
