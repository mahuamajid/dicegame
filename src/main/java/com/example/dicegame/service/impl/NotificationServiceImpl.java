package com.example.dicegame.service.impl;

import com.example.dicegame.config.KafkaTopicProperties;
import com.example.dicegame.constant.GameStateType;
import com.example.dicegame.model.event.PrizeEvent;
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
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    @Async()
    public void send(String gameName, String data, GameStateType gameStateType) {
        try {
            PrizeEvent event = PrizeEvent.builder()
                    .gameName(gameName)
                    .data(data)
                    .gameStateType(gameStateType)
                    .timestamp(System.currentTimeMillis())
                    .build();
            log.info("{} event Topic: {}", gameStateType, kafkaTopicProperties.getPrizeTopic());
            kafkaTemplate.send(kafkaTopicProperties.getPrizeTopic(), gameName, event);
            log.info("{} event produced to Kafka for game: {}", gameStateType, gameName);
        } catch (Exception e) {
            log.error("Failed to send {} event to Kafka for game: {}", gameStateType, gameName, e);
        }
    }
}
