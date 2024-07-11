package com.bank.account.service;

import com.bank.account.domain.AccountEntity;
import com.bank.account.dto.TransferDto;
import com.bank.account.repository.AccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.bank.account.dto.KafkaTopics.ACCOUNT_RESPONSE_TOPIC;
import static com.bank.account.dto.KafkaTopics.COMPENSATION_ACCOUNT_REQUEST_TOPIC;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final OutboxService outboxService;
    private final ObjectMapper mapper;
    @Transactional
    public void doTransaction(TransferDto transferDto)  {
        log.info("start doTransaction");
        try{
            //check customer account balance
            Optional<AccountEntity> optionalSenderEntity = accountRepository.findByAccountNumber(transferDto.getSenderAccountNumber());
            Optional<AccountEntity> optionalReceiverEntity = accountRepository.findByAccountNumber(transferDto.getReceiverAccountNumber());
            if(optionalSenderEntity.isPresent() && optionalReceiverEntity.isPresent()){
                // Do Credit and Debit
                AccountEntity senderEntity = optionalSenderEntity.get();
                AccountEntity receiverEntity = optionalReceiverEntity.get();
                log.info("senderAccount :{}",senderEntity);
                log.info("receiverEntity :{}",receiverEntity);
                if(senderEntity.getAvailableBalance() >= transferDto.getTransferAmount()){
                    senderEntity.setAvailableBalance(senderEntity.getAvailableBalance()- transferDto.getTransferAmount());
                    receiverEntity.setAvailableBalance(receiverEntity.getAvailableBalance()+transferDto.getTransferAmount());
                    accountRepository.save(senderEntity);
                    accountRepository.save(receiverEntity);
                    // Save outbox message
                    outboxService.saveMessage(transferDto.getPaymentId().toString(),ACCOUNT_RESPONSE_TOPIC,mapper.writeValueAsString(transferDto));
                    log.info("doTransaction completed");
                }else{
                    throw new Exception("No enough balance at sender account"+transferDto.getSenderAccountNumber());
                }
            }else{
                throw new Exception("No data found");
            }
        }catch (Exception e){
            // Save outbox message
            try {
                outboxService.saveMessage(transferDto.getPaymentId().toString(),COMPENSATION_ACCOUNT_REQUEST_TOPIC,mapper.writeValueAsString(transferDto));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            e.printStackTrace();
        }
    }
}
