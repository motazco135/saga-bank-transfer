package com.bank.trasfer.routers;

import com.bank.trasfer.domain.dto.TransferState;
import com.bank.trasfer.domain.service.InboxService;
import com.bank.trasfer.domain.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.bank.trasfer.domain.dto.KafkaTopics.*;

@RequiredArgsConstructor
@Component
public class TransferSagaRoute extends RouteBuilder {

    private final TransferService transferService;
    private final InboxService inboxService;

    @Override
    public void configure() throws Exception {
        //here we will configure our saga process flow
        from("kafka:"+PAYMENT_REQUEST_TOPIC+"?groupId=payment-request-consumer-group" +
                "&autoOffsetReset=latest")
                .process(exchange -> {
                    String paymentId = exchange.getIn().getHeader("kafka.KEY", String.class);
                    String paymentDetails = exchange.getIn().getBody(String.class);
                    if (!inboxService.isProcessed(paymentId,PAYMENT_REQUEST_TOPIC)) {
                        // Store payment request in the database
                        transferService.processTransferRequest(paymentDetails);

                        // Save the state to the database
                        inboxService.markAsProcessed(paymentId,PAYMENT_REQUEST_TOPIC,paymentDetails);
                        exchange.getIn().setBody(paymentDetails);
                        exchange.getIn().setHeader("paymentId", paymentId);
                    } else {
                        exchange.setProperty(Exchange.ROUTE_STOP, true);
                    }
                }).to("kafka:"+FEE_REQUEST_TOPIC+"?brokers=#{{camel.component.kafka.brokers}}");

        //process fee response
        from("kafka:"+FEE_RESPONSE_TOPIC+"?groupId=fee-response-consumer-group" +
                "&autoOffsetReset=latest").process(exchange->{
                    //update payment details with fee amount
                    String paymentId = exchange.getIn().getHeader("kafka.KEY", String.class);
                    String paymentDetails = exchange.getIn().getBody(String.class);
                    if (!inboxService.isProcessed(paymentId,FEE_RESPONSE_TOPIC)) {
                        //update transfer details
                        transferService.updateFee(paymentDetails);

                        // Save the state to the database
                        inboxService.markAsProcessed(paymentId,FEE_RESPONSE_TOPIC,paymentDetails);
                        exchange.getIn().setBody(paymentDetails);
                        exchange.getIn().setHeader("paymentId", paymentId);
                    }
                }).to("kafka:"+ACCOUNT_REQUEST_TOPIC+"?brokers=#{{camel.component.kafka.brokers}}");//Continue to Account step

        // Compensation routes
        from("kafka:"+COMPENSATION_FEE_REQUEST_TOPIC+"?groupId=compensation-fee-request-group" +
                "&autoOffsetReset=latest")
                .process(exchange->{
                    String paymentId = exchange.getIn().getHeader("kafka.KEY", String.class);
                    String paymentDetails = exchange.getIn().getBody(String.class);
                    if (!inboxService.isProcessed(paymentId,COMPENSATION_FEE_REQUEST_TOPIC)) {
                        //mark transfer as failed
                        transferService.updateTransferStatus(UUID.fromString(paymentId), TransferState.FAILED);

                        // Save the state to the database
                        inboxService.markAsProcessed(paymentId,COMPENSATION_FEE_REQUEST_TOPIC,paymentDetails);
                    }
                });
    }
}
