package com.bank.fee.controller;

import com.bank.fee.dto.FeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class FeeController {

    @GetMapping
    public ResponseEntity<?> getFeeDetails(){
        return ResponseEntity.ok( FeeDto.builder().bankFeeAmount("20").VatAmount("1").build());
    }
}
