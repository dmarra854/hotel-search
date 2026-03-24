package com.hotel.search.infrastructure.adapter.out.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.search.TestFixtures;
import com.hotel.search.infrastructure.adapter.out.kafka.producer.KafkaSearchEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("KafkaSearchEventPublisher unit tests")
class KafkaSearchEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private KafkaSearchEventPublisher publisher;

    private static final String TOPIC = "hotel_availability_searches";

    @BeforeEach
    void setUp() {
        publisher = new KafkaSearchEventPublisher(kafkaTemplate, objectMapper, TOPIC);
    }

    @Test
    @DisplayName("should send serialised message to the correct topic with searchId as key")
    void shouldSendMessageToKafka() throws Exception {
        String payload = "{\"searchId\":\"test\"}";
        given(objectMapper.writeValueAsString(TestFixtures.SEARCH)).willReturn(payload);

        assertDoesNotThrow(() -> publisher.publish(TestFixtures.SEARCH));

        verify(kafkaTemplate).send(eq(TOPIC), eq(TestFixtures.SEARCH_ID), eq(payload));
    }

    @Test
    @DisplayName("should throw RuntimeException when JSON serialisation fails")
    void shouldThrowWhenSerialisationFails() throws Exception {
        given(objectMapper.writeValueAsString(any()))
                .willThrow(new JsonProcessingException("error") {});

        assertThatThrownBy(() -> publisher.publish(TestFixtures.SEARCH))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining(TestFixtures.SEARCH_ID);
    }
}
