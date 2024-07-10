package com.bank.trasfer.domain.repository;

import com.bank.trasfer.domain.InboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxMessageEntity, Long> {
    boolean existsByPaymentId(UUID paymentId);
}
