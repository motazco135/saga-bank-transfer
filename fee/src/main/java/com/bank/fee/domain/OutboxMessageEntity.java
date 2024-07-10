package com.bank.fee.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="t_outbox_message")
public class OutboxMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;//denotes the kind of entity we’re dealing with

    @Column(nullable = false,columnDefinition = "VARCHAR(255)")
    private String payload;

    private UUID PaymentId;
    private String status;//message status

    @Column(updatable = false, name = "created_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "processed_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime processedAt;

    @PrePersist
    void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    void setProcessedAt(){
        this.processedAt = LocalDateTime.now();
    }
}
