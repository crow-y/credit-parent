package tech.fullink.credit.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author crow
 */
@Configuration
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
public class AppConfig {
}
