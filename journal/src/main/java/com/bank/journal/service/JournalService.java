package com.bank.journal.service;


import com.bank.journal.domain.JournalEntriesEntity;
import com.bank.journal.dto.KafkaTopics;
import com.bank.journal.dto.TransferDto;
import com.bank.journal.repository.JournalEntriesRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntriesRepository journalEntriesRepository;
    private final ObjectMapper mapper;
    private final OutboxService outboxService;

    @Transactional
    public void createJournalEntry(TransferDto transferDto){
        try {
            JournalEntriesEntity entity = new JournalEntriesEntity();
            entity.setDescription(transferDto.getTransferReason());
            entity.setDebitAmount(transferDto.getTransferAmount()+transferDto.getFee());
            entity.setDebitAccountNumber(transferDto.getSenderAccountNumber());
            entity.setCreditAccountNumber(transferDto.getReceiverAccountNumber());
            entity.setCreditAmount(transferDto.getTransferAmount());
            journalEntriesRepository.save(entity);
            // Save outbox message
            outboxService.saveMessage(transferDto.getPaymentId().toString(), KafkaTopics.JOURNAL_RESPONSE_TOPIC,mapper.writeValueAsString(transferDto));
            log.info("createJournalEntry completed for paymentId: {}",transferDto.getPaymentId());
        }catch (Exception e){
            // Save outbox message
            try {
                outboxService.saveMessage(transferDto.getPaymentId().toString(),KafkaTopics.COMPENSATION_JOURNAL_REQUEST_TOPIC,mapper.writeValueAsString(transferDto));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }

    }
}
