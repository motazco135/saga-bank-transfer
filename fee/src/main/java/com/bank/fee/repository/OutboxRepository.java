package com.bank.fee.repository;

import com.bank.fee.domain.OutboxMessageEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessageEntity, Long> {
    @Query("SELECT o FROM OutboxMessageEntity o WHERE o.status = 'PENDING'")
    List<OutboxMessageEntity> findPendingMessages();
}
