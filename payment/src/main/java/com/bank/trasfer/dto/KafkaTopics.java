package com.bank.trasfer.dto;

public class KafkaTopics {

    public static final String PAYMENT_REQUEST_TOPIC = "payment-request";

    public static final String FEE_REQUEST_TOPIC="fee-request";
    public static final String FEE_RESPONSE_TOPIC="fee-response";
    public static final String COMPENSATION_FEE_REQUEST_TOPIC="compensation-fee-request";

    public static final String ACCOUNT_REQUEST_TOPIC="account-request";
    public static final String ACCOUNT_RESPONSE_TOPIC="account-response";
    public static final String COMPENSATION_ACCOUNT_REQUEST_TOPIC="compensation-account-request";

    public static final String JOURNAL_REQUEST_TOPIC= "journal-request";
    public static final String JOURNAL_RESPONSE_TOPIC= "journal-response";
    public static final String COMPENSATION_JOURNAL_REQUEST_TOPIC = "compensation-journal-request";
}
