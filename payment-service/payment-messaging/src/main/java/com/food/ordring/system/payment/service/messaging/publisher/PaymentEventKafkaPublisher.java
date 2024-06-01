package com.food.ordring.system.payment.service.messaging.publisher;

import com.food.ordring.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordring.system.kafka.producer.KafkaMessageHelper;
import com.food.ordring.system.kafka.producer.service.KafkaProducer;
import com.food.ordring.system.payment.service.domain.config.PaymentServiceConfigData;
import com.food.ordring.system.payment.service.domain.event.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventKafkaPublisher<T extends PaymentEvent> {


    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;

    private final PaymentServiceConfigData paymentServiceConfigData;

    private final KafkaMessageHelper kafkaMessageHelper;





    public void publish(T paymentEvent, Function<T,PaymentResponseAvroModel> mapper) {


        String orderId = paymentEvent.getPayment().getOrderId().toString();
        log.info("Received {} for order id: {} ", paymentEvent.getClass().getName(), orderId);

        PaymentResponseAvroModel paymentResponseAvroModel = mapper.apply(paymentEvent);


        String responseTopicName = paymentServiceConfigData.getPaymentResponseTopicName();

        try {
            kafkaProducer.send(responseTopicName,
                    orderId,
                    paymentResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
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




}
