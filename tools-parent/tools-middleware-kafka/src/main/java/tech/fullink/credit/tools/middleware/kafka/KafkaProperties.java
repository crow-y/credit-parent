package tech.fullink.credit.tools.middleware.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author crow
 */
@Data
@ConfigurationProperties(prefix = "topic.kafka")
public class KafkaProperties {

    private String server;
    private String retries = "0";
    private String batchSize = "16384";
    private String lingerMs = "1";
    private String bufferMemory = "33554432";

    private String autoCommitIntervalMs = "100";
    private String timeoutMs = "15000";
    private String group = "default-group";
    private String offsetConfig = "latest";

    /**
     * 默认消费者配置
     *
     * @return
     */
    public Map<String, Object> defaultConsumerConfigs() {
        Map<String, Object> propsMap = new HashMap<>(16);
        propsMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.server);
        propsMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        propsMap.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, this.autoCommitIntervalMs);
        propsMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, timeoutMs);
        propsMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        propsMap.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        propsMap.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetConfig);
        return propsMap;
    }

    /**
     * 默认生产者配置
     *
     * @return
     */
    public Map<String, Object> dProducerConfigs() {
        Map<String, Object> props = new HashMap<>(16);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.server);
        props.put(ProducerConfig.RETRIES_CONFIG, this.retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, this.batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, this.lingerMs);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, this.bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
}
