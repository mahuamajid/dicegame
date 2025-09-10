package com.example.dicegame.service.impl;

import com.example.dicegame.config.KafkaTopicProperties;
import com.example.dicegame.model.event.NotificationEvent;
import com.example.dicegame.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    @Async()
    public void send(NotificationEvent event) {
        try {
            log.info("{} event Topic: {}", event.getGameStateType(), kafkaTopicProperties.getGameTopic());
            kafkaTemplate.send(kafkaTopicProperties.getGameTopic(), event.getGameName(), event);
            log.info("{} event produced to Kafka for game: {}", event.getGameStateType(), event.getGameName());
        } catch (Exception e) {
            log.error("Failed to send {} event to Kafka for game: {}", event.getGameStateType(), event.getGameName(), e);
        }
    }
}
