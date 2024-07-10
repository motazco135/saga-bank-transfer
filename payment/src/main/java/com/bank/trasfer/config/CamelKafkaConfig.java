package com.bank.trasfer.config;

import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.component.ComponentsBuilderFactory;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.component.kafka.KafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@RequiredArgsConstructor
//@Configuration
//public class CamelKafkaConfig {
//
//    @Bean
//    public KafkaComponent kafka(){
//        return ComponentsBuilderFactory.kafka()
//                .breakOnFirstError(true)
//                .build();
//    }
//
//    @Bean("KafkaConfiguration")
//    public KafkaConfiguration kafkaconfiguration(){
//        KafkaConfiguration config = new KafkaConfiguration();
//        return  config;
//    }
//}
