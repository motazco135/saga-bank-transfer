package com.bank.fee.service;

import com.bank.fee.domain.InboxMessageEntity;
import com.bank.fee.repository.InboxRepository;
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

    public boolean isProcessed(String paymentId) {
        boolean result = inboxRepository.existsByPaymentId(UUID.fromString(paymentId));
        log.info("isProcessed paymentId:{} , result : {} ",paymentId,result);
        return result;
    }

    @Transactional
    public void markAsProcessed(String paymentId) {
        InboxMessageEntity message = new InboxMessageEntity();
        message.setPaymentId(UUID.fromString(paymentId));
        inboxRepository.save(message);
    }
}