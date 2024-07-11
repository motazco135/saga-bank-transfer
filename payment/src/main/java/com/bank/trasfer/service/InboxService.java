package com.bank.trasfer.service;

import com.bank.trasfer.domain.InboxMessageEntity;
import com.bank.trasfer.dto.InBoxStatus;
import com.bank.trasfer.domain.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;

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

    @Transactional
    public void markAsProcessed(String paymentId , String aggregateType, String payload) {
        InboxMessageEntity message = new InboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        message.setPayload(payload);
        message.setAggregateType(aggregateType);
        message.setStatus(InBoxStatus.PROCESSED.toString());
        message.setProcessedAt(LocalDateTime.now());
        inboxRepository.save(message);
    }
}
