package com.hotel.search.infrastructure.adapter.out.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.domain.model.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaSearchConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaSearchConsumer.class);

    private final SearchRepository searchRepository;
    private final ObjectMapper objectMapper;

    public KafkaSearchConsumer(SearchRepository searchRepository,
                               ObjectMapper objectMapper) {
        this.searchRepository = searchRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(
            topics = "${hotel.search.kafka.topic:hotel-search-events}",
            groupId = "${spring.kafka.consumer.group-id:hotel-search-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(String message) {
        try {
            Search search = objectMapper.readValue(message, Search.class);
            searchRepository.save(search);
            log.info("Persisted search event for searchId={}", search.searchId());
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialise Kafka message: {}", message, e);
        }
    }
}
