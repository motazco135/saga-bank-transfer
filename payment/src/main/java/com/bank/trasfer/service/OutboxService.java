package com.bank.trasfer.service;

import com.bank.trasfer.dto.OutBoxStatus;
import com.bank.trasfer.domain.OutboxMessageEntity;
import com.bank.trasfer.domain.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final CamelContext camelContext;
    private final OutboxRepository outboxRepository;

    @Transactional
    public void saveMessage(UUID paymentId, String aggregateType, String payload) {
        OutboxMessageEntity message = new OutboxMessageEntity();
        message.setPaymentId(paymentId);
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
            camelContext.createProducerTemplate()
                    //.sendBodyAndHeader("kafka:" + message.getAggregateType()+"?key="+message.getPaymentId(),message.getPayload(), KafkaConstants.KEY,message.getPaymentId());
                    .sendBody("kafka:" + message.getAggregateType()+"?key="+message.getPaymentId(), message.getPayload());
            message.setStatus(OutBoxStatus.PROCESSED.toString());
            outboxRepository.save(message);
        }
    }
}
