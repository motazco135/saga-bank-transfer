package com.bank.account.service;

import com.bank.account.domain.OutboxMessageEntity;
import com.bank.account.dto.OutBoxStatus;
import com.bank.account.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final OutboxRepository outboxRepository;

    @Transactional
    public void saveMessage(String paymentId, String aggregateType, String payload) {
        OutboxMessageEntity message = new OutboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        message.setAggregateType(aggregateType);
        message.setPayload(payload);
        message.setStatus(OutBoxStatus.PENDING.toString());
        outboxRepository.save(message);
    }

    @Scheduled(fixedRate = 5000)
    public void processOutboxMessages() {
        List<OutboxMessageEntity> messages = outboxRepository.findPendingMessages();
        for (OutboxMessageEntity message : messages) {
            log.info("send to kafka topic name: {} paymentId: {}",message.getAggregateType(),message.getPaymentId());
            kafkaTemplate.send(message.getAggregateType(), message.getPaymentId().toString(), message.getPayload());
            message.setStatus(OutBoxStatus.PROCESSED.toString());
            outboxRepository.save(message);
        }
    }
}

