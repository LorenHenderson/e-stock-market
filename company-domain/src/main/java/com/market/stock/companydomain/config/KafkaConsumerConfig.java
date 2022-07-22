package com.market.stock.companydomain.config;

import com.market.stock.companydomain.domain.SaveStockEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {
    private static final String bootstrapServer = "localhost:9092";

    public ConsumerFactory<String, SaveStockEvent> saveStockEventConsumerFactory(){
       Map<String, Object> props = new HashMap<>();
       props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG ,bootstrapServer );
       props.put(ConsumerConfig.GROUP_ID_CONFIG, "companyUpdate");

       return new DefaultKafkaConsumerFactory<>(
               props,
               new StringDeserializer(),
               new JsonDeserializer<>(SaveStockEvent.class, false)
       );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SaveStockEvent> saveStockEventConcurrentKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, SaveStockEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(saveStockEventConsumerFactory());

        return factory;
    }
}
