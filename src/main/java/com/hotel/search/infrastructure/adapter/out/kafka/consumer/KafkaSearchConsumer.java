package com.hotel.search.infrastructure.adapter.out.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.domain.model.Search;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaSearchConsumer {

    private final SearchRepository searchRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${hotel.search.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consume(@Payload final String message) {
        try {
            Search search = objectMapper.readValue(message, Search.class);

            log.debug("Procesando evento de búsqueda para searchId: {}", search.searchId());

            searchRepository.save(search);

            log.info("Búsqueda persistida exitosamente: {}", search.searchId());

        } catch (JsonProcessingException e) {
            log.error("Error al deserializar el mensaje de Kafka: {}", message, e);
        }
    }
}