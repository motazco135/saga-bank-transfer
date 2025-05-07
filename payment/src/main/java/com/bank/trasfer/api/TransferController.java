package com.bank.trasfer.api;

import com.bank.trasfer.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transfer")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> createTransferRequest(@RequestBody CreateTransferRequest createTransferRequest) throws JsonProcessingException {
        // Basic validation
        if(createTransferRequest.getTransferAmount()==0
        || createTransferRequest.getReceiverAccountNumber() == null
        || createTransferRequest.getSenderAccountNumber() == null)
        {
            return ResponseEntity.badRequest().body("Invalid request");
        }else{
            CreateTransferResponse transferResponse = transferService.createTransferRequest(createTransferRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(transferResponse);
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getTransferRequest(@PathVariable UUID paymentId) throws JsonProcessingException {
        Optional<TransferStatusResponseDto> transferStatusResponseDto =  transferService.getTransferStatus(paymentId);
        if(transferStatusResponseDto.isPresent()){
            return ResponseEntity.ok(transferStatusResponseDto.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
