package rating.engine.infrastructure.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.kafka.autoconfigure.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.Map;

import static java.lang.Boolean.TRUE;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfiguration {

    private final KafkaProperties kafkaProperties;
    private final KafkaConfigProperties kafkaConfigProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> billingLineDataKafkaListener(ConsumerFactory<String, Object> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setAutoStartup(TRUE);

        // Enable batch processing
        factory.setBatchListener(true);
        factory.setConcurrency(6);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.BATCH);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(defaultProperties());
    }

    private Map<String, Object> defaultProperties() {
        Map<String, Object> properties = kafkaProperties.buildConsumerProperties();
        properties.putAll(kafkaConfigProperties.buildCommonProperties());

        return properties;
    }

}
