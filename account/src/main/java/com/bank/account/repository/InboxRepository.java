package com.bank.account.repository;

import com.bank.account.domain.InboxMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface InboxRepository extends JpaRepository<InboxMessageEntity, Long> {

    boolean existsByPaymentIdAndAggregateType(UUID paymentId,String aggregateType);

    @Query("SELECT o FROM InboxMessageEntity o WHERE o.status = 'PENDING'")
    List<InboxMessageEntity> findPendingMessages();
}
