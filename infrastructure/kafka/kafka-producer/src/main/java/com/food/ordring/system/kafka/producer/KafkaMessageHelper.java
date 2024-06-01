package com.food.ordring.system.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordring.system.order.service.domain.exception.OrderDomainException;
import com.food.ordring.system.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.BiConsumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaMessageHelper {


    private final ObjectMapper objectMapper;


    public <T, U> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(String topicName, T avroModel,
                                                                                   U outboxMessage,
                                                                                   BiConsumer<U, OutboxStatus> outboxBiConsumer,
                                                                                   String orderId) {
        return new ListenableFutureCallback<SendResult<String, T>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("Error while sending {} with message: {} and outbox type: {}, topic {}",
                        avroModel.getClass().getName(),
                        outboxMessage.getClass().getName(),
                        avroModel,
                        topicName);

                outboxBiConsumer.accept(outboxMessage, OutboxStatus.FAILED);
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

                outboxBiConsumer.accept(outboxMessage, OutboxStatus.COMPLETED);
            }
        };
    }


    public  <T> T getEventPayload(String payload, Class<T> outputTypeClass) {
        try {
            return objectMapper.readValue(payload, outputTypeClass);
        } catch (JsonProcessingException e) {
            log.error("Could not read {} object!", outputTypeClass.getName(), e);
            throw new OrderDomainException("Could not read " + outputTypeClass.getName() + " object!", e);
        }
    }
}
