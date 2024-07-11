package com.bank.journal.service;

import com.bank.journal.domain.InboxMessageEntity;
import com.bank.journal.dto.InBoxStatus;
import com.bank.journal.dto.TransferDto;
import com.bank.journal.repository.InboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;



@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;
    private final JournalService journalService;
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
            log.info("processInBoxMessages paymentId: {}",message.getPaymentId());
            // do createJournalEntry
            TransferDto transferDto = mapper.readValue(message.getPayload(), TransferDto.class);
            journalService.createJournalEntry(transferDto);
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