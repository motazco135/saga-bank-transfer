package com.bank.trasfer.domain;

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
    private String aggregateType;//denotes the kind of entity we’re dealing with

    @Column(nullable = false,columnDefinition = "VARCHAR(255)")
    private String payload;

    private String status;//message status

    @Column(updatable = false, name = "processed_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime processedAt;

    @Column(updatable = false, name = "created_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @PreUpdate
    void setProcessedAt(){
        this.processedAt = LocalDateTime.now();
    }

    @PrePersist
    void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }
}
