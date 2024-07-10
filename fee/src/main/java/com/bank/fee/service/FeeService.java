package com.bank.fee.service;

import com.bank.fee.dto.TransferDto;
import com.bank.fee.dto.TransferState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bank.fee.dto.KafkaTopics.*;
@Slf4j
@Component
@RequiredArgsConstructor
public class FeeService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final InboxService inboxService;
    private final OutboxService outboxService;

    @KafkaListener(topics = FEE_REQUEST_TOPIC,
            containerFactory = "kafkaListenerContainerFactory")
    public void consumer(@Payload String  paymentDetails, @Headers Map<String, Object> headers , Acknowledgment acknowledgment) throws JsonProcessingException {
        String paymentId = (String) headers.get(KafkaHeaders.RECEIVED_KEY);
        try {
            // Calculate fee logic
            log.info("calculateFee  paymentId: {} ,paymentDetails: {}",paymentId,paymentDetails);
            ObjectMapper objectMapper = new ObjectMapper();
            TransferDto transferDto = objectMapper.readValue(paymentDetails, TransferDto.class);
            log.info("TransferDto : {}",transferDto);
            if (!inboxService.isProcessed(paymentId)) {
                // Calculate fee
                if(transferDto.getTransferAmount()>100){
                    transferDto.setFee(5);
                }else{
                    transferDto.setFee(1);
                }
                transferDto.setState(TransferState.PROCESSING);

                // Save outbox message
                outboxService.saveMessage(transferDto.getPaymentId(),FEE_RESPONSE_TOPIC,objectMapper.writeValueAsString(transferDto));

                // Save the state to the database
                inboxService.markAsProcessed(paymentId);
                acknowledgment.acknowledge();
            }
        } catch (Exception e) {
            // Publish compensation message
            kafkaTemplate.send(COMPENSATION_FEE_REQUEST_TOPIC, paymentId, paymentDetails);
            e.printStackTrace();
            throw e;
        }
    }

}
