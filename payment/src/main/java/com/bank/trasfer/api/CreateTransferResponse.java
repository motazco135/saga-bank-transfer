package com.bank.trasfer.api;

import com.bank.trasfer.dto.TransferState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransferResponse {
    private UUID paymentId;
    private TransferState state;
}
