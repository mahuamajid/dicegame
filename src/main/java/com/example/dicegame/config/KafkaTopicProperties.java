package com.example.dicegame.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopicProperties {
    private String prizeTopic;
}
