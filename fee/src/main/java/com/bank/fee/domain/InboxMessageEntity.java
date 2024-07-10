package com.bank.fee.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="t_inbox_message")
public class InboxMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID paymentId;

    @Column(updatable = false, name = "processed_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime processedAt;

    @PrePersist
    void setProcessedAt(){
        this.processedAt = LocalDateTime.now();
    }
}
