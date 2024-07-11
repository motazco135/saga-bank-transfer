package com.bank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private UUID paymentId;
    private UUID requestId;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private int transferAmount;
    private String transferReason;
    private TransferState state;
    private int fee;
}
