package com.hotel.search.infrastructure.adapter.out.kafka;

import com.hotel.search.infrastructure.config.KafkaConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaConfigTest {

    private KafkaConfig kafkaConfig;

    @BeforeEach
    void setUp() {
        kafkaConfig = new KafkaConfig();
        ReflectionTestUtils.setField(kafkaConfig, "bootstrapServers", "localhost:9092");
        ReflectionTestUtils.setField(kafkaConfig, "groupId", "test-group");
    }

    @Test
    void producerFactoryShouldHaveCorrectOptimizedConfigs() {
        ProducerFactory<String, String> factory = kafkaConfig.producerFactory();
        Map<String, Object> props = factory.getConfigurationProperties();

        assertThat(props.get(ProducerConfig.ACKS_CONFIG)).isEqualTo("1");
        assertThat(props.get(ProducerConfig.LINGER_MS_CONFIG)).isEqualTo(5);
        assertThat(props.get(ProducerConfig.COMPRESSION_TYPE_CONFIG)).isEqualTo("snappy");
    }

    @Test
    void kafkaListenerContainerFactoryShouldBeInitialized() {
        var factory = kafkaConfig.kafkaListenerContainerFactory();
        assertThat(factory).isNotNull();
        assertThat(factory.getContainerProperties().getListenerTaskExecutor()).isNotNull();
    }
}