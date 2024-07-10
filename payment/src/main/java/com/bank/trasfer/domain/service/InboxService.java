package com.bank.trasfer.domain.service;

import com.bank.trasfer.domain.InboxMessageEntity;
import com.bank.trasfer.domain.dto.InBoxStatus;
import com.bank.trasfer.domain.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InboxService {

    private final InboxRepository inboxRepository;

//    public boolean isProcessed(String paymentId) {
//        boolean result = inboxRepository.existsByPaymentId(UUID.fromString(paymentId));
//        log.info("isProcessed paymentId:{} , result : {} ",paymentId,result);
//        return result;
//    }

//    public boolean isProcessed(String paymentId,String aggregateType,String status) {
//        boolean result = inboxRepository.existsByPaymentIdAndAggregateTypeAndStatus(UUID.fromString(paymentId),aggregateType,status);
//        log.info("isProcessed paymentId:{} , result : {} ",paymentId,result);
//        return result;
//    }

    public boolean isProcessed(String paymentId,String aggregateType) {
        boolean result = inboxRepository.existsByPaymentIdAndAggregateType(UUID.fromString(paymentId),aggregateType);
        log.info("isProcessed paymentId:{} , result : {} ",paymentId,result);
        return result;
    }

    @Transactional
    public void saveMessage(UUID paymentId, String aggregateType, String payload){
        InboxMessageEntity entity = new InboxMessageEntity();
        entity.setPaymentId(paymentId);
        entity.setPayload(payload);
        entity.setAggregateType(aggregateType);
        entity.setStatus(InBoxStatus.PENDING.toString());
        inboxRepository.save(entity);
    }

//    @Transactional
//    public void markAsProcessed(String paymentId) {
//        InboxMessageEntity message = new InboxMessageEntity();
//        message.setPaymentId(UUID.fromString(paymentId));
//        inboxRepository.save(message);
//    }

    @Transactional
    public void markAsProcessed(String paymentId , String aggregateType, String payload) {
        InboxMessageEntity message = new InboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        message.setPayload(payload);
        message.setAggregateType(aggregateType);
        message.setStatus(InBoxStatus.PROCESSED.toString());
        inboxRepository.save(message);
    }
}
