package com.bank.fee.service;

import com.bank.fee.domain.InboxMessageEntity;
import com.bank.fee.dto.InBoxStatus;
import com.bank.fee.dto.TransferDto;
import com.bank.fee.repository.InboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.bank.fee.dto.KafkaTopics.FEE_RESPONSE_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;
    private final OutboxService outboxService;
    private final FeeService feeService;
    private final ObjectMapper mapper;

    public boolean isProcessed(String paymentId,String aggregateType) {
        boolean result = inboxRepository.existsByPaymentIdAndAggregateType(UUID.fromString(paymentId),aggregateType);
        log.info("isProcessed paymentId:{} , result : {} ",paymentId,result);
        return result;
    }

    @Transactional
    public void saveInboxMessage(String paymentId , String aggregateType, String payload) {
        InboxMessageEntity message = new InboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        message.setPayload(payload);
        message.setAggregateType(aggregateType);
        message.setStatus(InBoxStatus.PENDING.toString());
        inboxRepository.save(message);
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void processInBoxMessages() throws JsonProcessingException {
        List<InboxMessageEntity> messages = inboxRepository.findPendingMessages();
        for (InboxMessageEntity message : messages) {
            // Get fee
            TransferDto transferDto = mapper.readValue(message.getPayload(), TransferDto.class);
            transferDto = feeService.getFeeAmount(transferDto);
            // Save outbox message
            outboxService.saveMessage(transferDto.getPaymentId().toString(),FEE_RESPONSE_TOPIC,mapper.writeValueAsString(transferDto));

            // Update inbox status
            message.setStatus(InBoxStatus.PROCESSED.toString());
            inboxRepository.save(message);
        }
    }

    @Transactional
    public void markAsFailed(String paymentId , String aggregateType, String payload) {
        InboxMessageEntity message = new InboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        message.setPayload(payload);
        message.setAggregateType(aggregateType);
        message.setStatus(InBoxStatus.FAILED.toString());
        inboxRepository.save(message);
    }
}