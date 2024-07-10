//package com.bank.trasfer.config;
//
//import lombok.RequiredArgsConstructor;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@RequiredArgsConstructor
//public class KafkaProducerConfig extends KafkaProperties.Producer{
//
//    private final KafkaProperties common;
//
//    @Bean
//    public ProducerFactory<?, ?> producerFactory() {
//        final var conf = new HashMap<>(this.common.buildProducerProperties(null));
//        conf.putAll(this.buildProperties(null));
//        conf.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        conf.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(conf);
//    }
//
//    @Bean
//    public KafkaTemplate<?, ?> kafkaTemplate() {
//        return new KafkaTemplate<>(this.producerFactory());
//    }
//
//}
