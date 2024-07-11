package com.bank.account.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="t_account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private String customerName;
    private int availableBalance;

    @Column(updatable = false, name = "created_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @PreUpdate
    void setUpdatedAt(){
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    void setCreatedAt(){
        this.createdAt = LocalDateTime.now();
    }


}
