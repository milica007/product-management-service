package com.smg.product.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.smg.product.dto.ProductKafkaDTO;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

@Component
public class ProductProducer extends KafkaProducer<ProductKafkaDTO> {

    private static final Logger logger = LoggerFactory.getLogger(ProductProducer.class);

    @Value("${kafka.topic.product}")
    private String productTopic;

    public ProductProducer(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    public void sendProductMessage(ProductKafkaDTO productKafkaDTO) {
        logger.info("Sending product message to topic '{}': {}", productTopic, productKafkaDTO);
        sendMessage(productTopic, productKafkaDTO);
    }
}
