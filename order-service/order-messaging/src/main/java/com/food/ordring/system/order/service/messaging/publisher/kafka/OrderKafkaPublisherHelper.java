package com.food.ordring.system.order.service.messaging.publisher.kafka;

import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
public class OrderKafkaPublisherHelper {


    public <T> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String responseTopicName, T requestAvroModel,String orderId) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending  "+requestAvroModel.getClass().getName()+
                        " message {}, topic {}", requestAvroModel, responseTopicName);
            }

            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();
                log.info("Received successful response from kafka for order id: {}, " +
                                " Topic: {}, Partition: {}, Offset: {}, Timestamp: {}",
                        orderId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp()
                );
            }
        };
    }
}
