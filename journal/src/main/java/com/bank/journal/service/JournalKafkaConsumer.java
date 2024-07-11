package com.bank.journal.service;

import com.bank.journal.dto.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.bank.journal.dto.KafkaTopics.COMPENSATION_JOURNAL_REQUEST_TOPIC;
import static com.bank.journal.dto.KafkaTopics.JOURNAL_REQUEST_TOPIC;

@Slf4j
@Component
@RequiredArgsConstructor
public class JournalKafkaConsumer {

    private final InboxService inboxService;
    private final OutboxService outboxService;

    @KafkaListener(topics = JOURNAL_REQUEST_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumer(@Payload String  paymentDetails, @Headers Map<String, Object> headers , Acknowledgment acknowledgment){
        String paymentId = (String) headers.get(KafkaHeaders.RECEIVED_KEY);
        try {
            log.info("AccountKafkaConsumer  paymentId: {} ,paymentDetails: {}",paymentId,paymentDetails);
            if (!inboxService.isProcessed(paymentId,JOURNAL_REQUEST_TOPIC)) {
                // Save the state to the database
                inboxService.saveInboxMessage(paymentId,JOURNAL_REQUEST_TOPIC,paymentDetails);
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            //mark message as failed
            inboxService.markAsFailed(paymentId,JOURNAL_REQUEST_TOPIC,paymentDetails);
            // Save compensation message to outbox table
            outboxService.saveMessage(paymentId,COMPENSATION_JOURNAL_REQUEST_TOPIC,paymentDetails);
            e.printStackTrace();
            throw e;
        }

    }
}
