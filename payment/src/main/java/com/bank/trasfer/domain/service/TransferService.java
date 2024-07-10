package com.bank.trasfer.domain.service;

import com.bank.trasfer.domain.dto.TransferState;
import com.bank.trasfer.api.CreateTransferRequest;
import com.bank.trasfer.api.CreateTransferResponse;
import com.bank.trasfer.domain.TransferEntity;
import com.bank.trasfer.domain.dto.TransferDto;
import com.bank.trasfer.domain.repository.TransferRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.bank.trasfer.domain.dto.KafkaTopics.PAYMENT_REQUEST_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final TransferRepository transferRepository;
    private final OutboxService outboxService;
    private final ObjectMapper mapper;

    @Transactional
    public CreateTransferResponse  createTransferRequest(CreateTransferRequest createTransferRequest) throws JsonProcessingException {

        // Create a unique payment ID
        UUID paymentId = UUID.randomUUID();
        TransferDto transferDto = TransferDto.builder()
                .paymentId(paymentId)
                .requestId(createTransferRequest.getRequestId())
                .transferAmount(createTransferRequest.getTransferAmount())
                .transferReason(createTransferRequest.getTransferReason())
                .senderAccountNumber(createTransferRequest.getSenderAccountNumber())
                .receiverAccountNumber(createTransferRequest.getReceiverAccountNumber())
                .state(TransferState.PENDING).build();

        // Save outbox message
        outboxService.saveMessage(paymentId,PAYMENT_REQUEST_TOPIC,mapper.writeValueAsString(transferDto));

        // Return response
        return CreateTransferResponse.builder()
                .paymentId(transferDto.getPaymentId())
                .state(transferDto.getState()).build();
    }

    @Transactional
    public void processTransferRequest(String paymentDetails ) throws JsonProcessingException {
        log.info("processTransferRequest payment details : {} ",paymentDetails);
        //convert json to object
        TransferDto transferDto = mapper.readValue(paymentDetails,TransferDto.class);

        //save Transfer in the DB
        TransferEntity transferEntity =  new TransferEntity();
        transferEntity.setPaymentId(transferDto.getPaymentId());
        transferEntity.setRequestId(transferDto.getRequestId());
        transferEntity.setReceiverAccountNumber(transferDto.getReceiverAccountNumber());
        transferEntity.setSenderAccountNumber(transferDto.getSenderAccountNumber());
        transferEntity.setTransferAmount(transferDto.getTransferAmount());
        transferEntity.setTransferReason(transferDto.getTransferReason());
        transferEntity.setState(TransferState.PROCESSING);
        transferRepository.save(transferEntity);
    }

    @Transactional
    public void updateFee(String paymentDetails) throws JsonProcessingException {
        log.info("updateFee payment-details : {} ",paymentDetails);
        //convert json to object
        TransferDto transferDto = mapper.readValue(paymentDetails,TransferDto.class);
       if(transferDto!= null && transferDto.getFee()>0){
          int updatedRecords = transferRepository.updateFeeAmount(transferDto.getFee(),transferDto.getPaymentId());
          log.info("updateFee updatedRecordsCount : {} ",updatedRecords);
       }
    }


    @Transactional
    public void updateTransferStatus(UUID paymentId,TransferState state){
        int updatedRecords = transferRepository.updateTransferStatus(paymentId,state);
        log.info("updateTransferStatus updatedRecordsCount : {} ",updatedRecords);
    }

}
