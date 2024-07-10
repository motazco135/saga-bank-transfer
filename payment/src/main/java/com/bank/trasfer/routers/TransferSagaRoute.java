package com.bank.trasfer.routers;

import com.bank.trasfer.domain.service.InboxService;
import com.bank.trasfer.domain.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static com.bank.trasfer.domain.dto.KafkaTopics.FEE_REQUEST_TOPIC;
import static com.bank.trasfer.domain.dto.KafkaTopics.PAYMENT_REQUEST_TOPIC;

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
                    if (!inboxService.isProcessed(paymentId)) {
                        // Store payment request in the database
                        transferService.processTransferRequest(paymentDetails);
                        // Save the state to the database
                        inboxService.markAsProcessed(paymentId);
                        exchange.getIn().setBody(paymentDetails);
                        exchange.getIn().setHeader("paymentId", paymentId);
                    } else {
                        exchange.setProperty(Exchange.ROUTE_STOP, true);
                    }
                }).to("kafka:"+FEE_REQUEST_TOPIC+"?brokers=#{{camel.component.kafka.brokers}}");
    }
}
