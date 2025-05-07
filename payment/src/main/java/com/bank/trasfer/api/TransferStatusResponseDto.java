package com.bank.trasfer.api;

import com.bank.trasfer.dto.TransferState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusResponseDto {
    private UUID paymentId;
    private TransferState state;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private int transferAmount;
    private String transferReason;
}
