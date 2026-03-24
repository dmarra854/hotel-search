package com.hotel.search.infrastructure.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.TestFixtures;
import com.hotel.search.application.port.out.SearchRepository;
import com.hotel.search.infrastructure.adapter.out.kafka.consumer.KafkaSearchConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaSearchConsumer unit tests")
class KafkaSearchConsumerTest {

    @Mock
    private SearchRepository searchRepository;

    @Mock
    private ObjectMapper objectMapper;

    private KafkaSearchConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new KafkaSearchConsumer(searchRepository, objectMapper);
    }

    @Test
    @DisplayName("should deserialise message and save to repository")
    void shouldDeserialiseAndSave() throws Exception {
        String message = "{\"searchId\":\"test\"}";
        given(objectMapper.readValue(eq(message), eq(com.hotel.search.domain.model.Search.class)))
                .willReturn(TestFixtures.SEARCH);

        consumer.consume(message);

        verify(searchRepository).save(TestFixtures.SEARCH);
    }

    @Test
    @DisplayName("should not throw and should not save when message is malformed JSON")
    void shouldHandleMalformedJson() throws Exception {
        String badMessage = "NOT_JSON";
        given(objectMapper.readValue(eq(badMessage), eq(com.hotel.search.domain.model.Search.class)))
                .willThrow(new JsonProcessingException("bad json") {});

        assertDoesNotThrow(() -> consumer.consume(badMessage));
        verify(searchRepository, never()).save(any());
    }
}
