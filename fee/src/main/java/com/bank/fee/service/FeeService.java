package com.bank.fee.service;

import com.bank.fee.dto.TransferDto;
import com.bank.fee.dto.TransferState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FeeService {

    public TransferDto getFeeAmount(TransferDto transferDto){
        if(transferDto.getTransferAmount()>100){
            transferDto.setFee(5);
        }else{
            transferDto.setFee(1);
        }
        transferDto.setState(TransferState.PROCESSING);
        return transferDto;
    }
}
