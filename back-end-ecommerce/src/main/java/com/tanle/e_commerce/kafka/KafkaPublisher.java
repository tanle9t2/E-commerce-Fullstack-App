package com.tanle.e_commerce.kafka;

import com.tanle.e_commerce.dto.OrderDTO;
import com.tanle.e_commerce.event.OrderEvent;
import com.tanle.e_commerce.event.OrderStatus;
import com.tanle.e_commerce.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaPublisher {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    private final String TOPIC = "payment-topic";

    public KafkaPublisher(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendPaymentConfirmation(PaymentEvent paymentEvent) {
        System.out.println("publish payment event");
        kafkaTemplate.send(TOPIC, String.valueOf(paymentEvent.getPaymentRequestDto().getUserId()), paymentEvent);
    }
}
