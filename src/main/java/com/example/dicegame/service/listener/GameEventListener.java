package com.example.dicegame.service.listener;

import com.example.dicegame.model.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameEventListener {

    @KafkaListener(topics = "game-topic", groupId = "dice-game-group")
    public void message(NotificationEvent event) {
        log.info(event.getData());
    }
}
