package com.bank.journal.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="t_journal_entries")
public class JournalEntriesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String debitAccountNumber;
    private String creditAccountNumber;
    private int debitAmount;
    private int creditAmount;

    @Column(updatable = false, name = "created_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @PrePersist
    void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }
}
