package com.bank.trasfer.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferRequest {
    private UUID requestId;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private int transferAmount;
    private String transferReason;
}
