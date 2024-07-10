package com.bank.trasfer.api;

import com.bank.trasfer.domain.service.TransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
}
