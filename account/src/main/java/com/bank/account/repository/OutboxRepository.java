package com.bank.account.repository;

import com.bank.account.domain.OutboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxMessageEntity, Long> {
    @Query("SELECT o FROM OutboxMessageEntity o WHERE o.status = 'PENDING'")
    List<OutboxMessageEntity> findPendingMessages();
}
