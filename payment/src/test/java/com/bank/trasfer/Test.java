package com.bank.trasfer;

import com.bank.trasfer.domain.dto.TransferDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

    public static void main(String[] args) throws JsonProcessingException {
        String jsonString = "\"{\\\"paymentId\\\":\\\"f3be0da3-b8c2-4820-ad69-d4718148c4ee\\\",\\\"requestId\\\":\\\"3fa85f64-5717-4562-b3fc-2c963f66afa6\\\",\\\"senderAccountNumber\\\":\\\"test\\\",\\\"receiverAccountNumber\\\":\\\"test\\\",\\\"transferAmount\\\":100,\\\"transferReason\\\":\\\"test\\\",\\\"state\\\":\\\"PENDING\\\"}\"";
        ObjectMapper objectMapper = new ObjectMapper();
        TransferDto transferDto = objectMapper.readValue(jsonString, TransferDto.class);
        System.out.println(transferDto);
    }
}
