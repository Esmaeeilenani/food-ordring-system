package com.food.ordring.system.payment.service.messaging.listener;

import com.food.ordring.system.kafka.consumer.KafkaConsumer;
import com.food.ordring.system.kafka.order.avro.model.PaymentOrderStatus;
import com.food.ordring.system.kafka.order.avro.model.PaymentRequestAvroModel;
import com.food.ordring.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordring.system.payment.service.domain.ports.input.listener.PaymentRequestListener;
import com.food.ordring.system.payment.service.messaging.mapper.PaymentMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class PaymentRequestKafkaListener implements KafkaConsumer<PaymentRequestAvroModel> {
    private final PaymentMessagingDataMapper paymentMessagingDataMapper;

    private final PaymentRequestListener paymentRequestListener;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.payment-consumer-group-id}",
            topics = "${payment-service.payment-request-topic-name}"
    )
    public void receive(
            @Payload List<PaymentRequestAvroModel> messages,
            @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
            @Header(KafkaHeaders.OFFSET) List<Long> offset) {

        log.info("{} number of payment response received with keys:{}, partitions:{}, and offsets:{}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offset.toString());


        messages.forEach(this::processMessage);


    }

    private void processMessage(PaymentRequestAvroModel paymentRequestAvroModel) {
        PaymentRequest paymentRequest = paymentMessagingDataMapper.paymentRequestAvroModelToPaymentRequest(paymentRequestAvroModel);
        if (paymentRequestAvroModel.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)) {
            log.info("Processing payment for order id: {}", paymentRequestAvroModel.getOrderId());
            paymentRequestListener.completePayment(paymentRequest);
            return;
        }
        log.info("cancelling payment for order id: {}", paymentRequestAvroModel.getOrderId());
        paymentRequestListener.cancelPayment(paymentRequest);


    }

}
