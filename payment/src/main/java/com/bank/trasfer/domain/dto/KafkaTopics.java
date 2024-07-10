package com.bank.trasfer.domain.dto;

public class KafkaTopics {

    public static final String PAYMENT_REQUEST_TOPIC = "payment-request";
    public static final String FEE_REQUEST_TOPIC="fee-request";
    public static final String ACCOUNT_REQUEST_TOPIC="account-request";

    public static final String FEE_RESPONSE_TOPIC="fee-response";
    public static final String COMPENSATION_FEE_REQUEST_TOPIC="compensation-fee-request";
}
