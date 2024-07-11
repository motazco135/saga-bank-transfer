package com.bank.fee.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.bank.fee.dto.KafkaTopics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeeKafkaConsumer {

    private final InboxService inboxService;
    private final OutboxService outboxService;

    @KafkaListener(topics = FEE_REQUEST_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumer(@Payload String  paymentDetails, @Headers Map<String, Object> headers , Acknowledgment acknowledgment)  {
        String paymentId = (String) headers.get(KafkaHeaders.RECEIVED_KEY);
        try {
            // Calculate fee logic
            log.info("calculateFee  paymentId: {} ,paymentDetails: {}",paymentId,paymentDetails);
            if (!inboxService.isProcessed(paymentId,FEE_REQUEST_TOPIC)) {
                // Save the state to the database
                inboxService.saveInboxMessage(paymentId,FEE_REQUEST_TOPIC,paymentDetails);
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            //mark message as failed
            inboxService.markAsFailed(paymentId,FEE_REQUEST_TOPIC,paymentDetails);
            // Save compensation message to outbox table
            outboxService.saveMessage(paymentId,COMPENSATION_FEE_REQUEST_TOPIC,paymentDetails);
            e.printStackTrace();
            throw e;
        }
    }

}
