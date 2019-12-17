package tech.fullink.credit.tools.middleware.kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

/**
 * @author crow
 */
@Configuration
@ConditionalOnProperty("topic.kafka.server")
@EnableConfigurationProperties({KafkaProperties.class})
@EnableKafka
public class KafkaContextAutoConfiguration {

    /**
     * 初始化 kafkaTemplate
     *
     * @param properties
     * @return
     */
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(KafkaProperties properties) {
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(properties.dProducerConfigs()));
    }

    /**
     * kafka消费者初始化
     *
     * @param properties
     * @return
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory(KafkaProperties properties) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(properties.defaultConsumerConfigs()));
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(30000);
        return factory;
    }
}
