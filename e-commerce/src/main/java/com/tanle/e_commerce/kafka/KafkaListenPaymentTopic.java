package com.tanle.e_commerce.kafka;

import com.tanle.e_commerce.event.OrderEvent;
import com.tanle.e_commerce.event.OrderStatus;
import com.tanle.e_commerce.event.PaymentEvent;
import com.tanle.e_commerce.event.PaymentStatus;
import com.tanle.e_commerce.service.EmailService;
import com.tanle.e_commerce.service.PaymentService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class KafkaListenPaymentTopic {
    @Autowired
    private EmailService emailService;

    @KafkaListener(topics = "payment-topic", groupId = "ecommerce")
    public void consumeEventAndPublisheTopic(List<PaymentEvent> paymentEvents) {
        System.out.println("consume");
        for (PaymentEvent event : paymentEvents) {
            if (event.getPaymentStatus().equals(PaymentStatus.PAYMENT_COMPLETED)) {
                emailService.sendConfirmPayment(event.getPaymentRequestDto().getEmail(),
                        event.getPaymentRequestDto());
            }
        }
    }
}
