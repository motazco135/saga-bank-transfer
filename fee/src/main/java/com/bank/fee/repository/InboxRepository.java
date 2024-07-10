package com.bank.fee.repository;

import com.bank.fee.domain.InboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxMessageEntity, Long> {
    boolean existsByPaymentId(UUID paymentId);
}
