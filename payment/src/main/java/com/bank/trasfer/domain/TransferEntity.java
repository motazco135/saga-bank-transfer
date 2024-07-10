package com.bank.trasfer.domain;

import com.bank.trasfer.domain.dto.TransferState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="t_transfer")
public class TransferEntity {

    @Id
    private UUID paymentId;

    private UUID requestId;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private int transferAmount;
    private String transferReason;
    private int feeAmount;

    @Enumerated(EnumType.STRING)
    private TransferState state;

    @Column(updatable = false, name = "created_date" , columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;

    @Column(name = "modified_date" , columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedDate;

    @PrePersist
    void setCreatedDate(){
        this.createdDate = LocalDateTime.now();
    }

    @PreUpdate
    void setModifiedDate(){
        this.modifiedDate = LocalDateTime.now();
    }
    public void fail() {
        this.state = TransferState.FAILED;
    }
}
