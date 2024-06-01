package com.food.ordring.system.order.service.messaging.listener.kafka;

import com.food.ordring.system.kafka.consumer.KafkaConsumer;
import com.food.ordring.system.kafka.order.avro.model.PaymentResponseAvroModel;
import com.food.ordring.system.kafka.order.avro.model.PaymentStatus;
import com.food.ordring.system.order.service.domain.ports.input.message.listener.payment.PaymentResponseListener;
import com.food.ordring.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class PaymentResponseKafkaListener implements KafkaConsumer<PaymentResponseAvroModel> {


    private final PaymentResponseListener paymentResponseListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;


    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${order-service.payment-response-topic-name}")
    @Override
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of payment response received with keys:{}, partitions:{}, and offsets:{}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {
            try {
                if (paymentResponseAvroModel.getPaymentStatus().equals(PaymentStatus.COMPLETED)) {
                    log.info("processing a successful payment for order id:{}", paymentResponseAvroModel.getOrderId());
                    paymentResponseListener
                            .paymentCompleted(orderMessagingDataMapper
                                    .paymentResponseAvroToPaymentResponse(paymentResponseAvroModel));
                }

                if (paymentResponseAvroModel.getPaymentStatus().equals(PaymentStatus.CANCELLED) ||
                        paymentResponseAvroModel.getPaymentStatus().equals(PaymentStatus.FAILED)) {
                    log.info("processing a cancelled payment for order id:{}", paymentResponseAvroModel.getOrderId());
                    paymentResponseListener
                            .paymentCanceled(orderMessagingDataMapper.paymentResponseAvroToPaymentResponse(paymentResponseAvroModel));
                }
            } catch (OptimisticLockingFailureException e) {
                log.error("Caught Optimistic Locking exception in PaymentResponseKafkaListener for order id: {}",
                        paymentResponseAvroModel.getOrderId());
            }


        });

    }
}
