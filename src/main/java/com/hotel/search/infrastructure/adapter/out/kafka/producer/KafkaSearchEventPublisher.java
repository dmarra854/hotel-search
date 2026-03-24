package com.hotel.search.infrastructure.adapter.out.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.application.port.out.SearchEventPublisher;
import com.hotel.search.domain.model.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchEventPublisher implements SearchEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public KafkaSearchEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
                                     ObjectMapper objectMapper,
                                     @Value("${hotel.search.kafka.topic}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    @Override
    public void publish(Search search) {
        try {
            String payload = objectMapper.writeValueAsString(search);
            kafkaTemplate.send(topic, search.searchId(), payload);
            log.info("Published search event for searchId={}", search.searchId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(
                    String.format("Failed to serialise search event for searchId=%s", search.searchId()), e);
        }
    }
}
