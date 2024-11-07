package com.smg.product.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaProducer<V extends Serializable> {

    private final KafkaTemplate<String, V> producer;

    @Autowired
    public KafkaProducer(KafkaProperties kafkaProperties) {
        this.producer = new KafkaTemplate<>(createProducerFactory(kafkaProperties));
    }

    private ProducerFactory<String, V> createProducerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.putAll(kafkaProperties.getProducer().getProperties());

        return new DefaultKafkaProducerFactory<>(props);
    }

    public void sendMessage(String topic, V data) {
        producer.send(topic, data);
    }
}

