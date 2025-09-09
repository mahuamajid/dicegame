package com.example.dicegame.service.listener;

import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.GamePlayer;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.model.event.NotificationEvent;
import com.example.dicegame.model.event.PrizeNotificationEvent;
import com.example.dicegame.repository.GamePlayerRepository;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameEventListener {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GamePlayerRepository gamePlayerRepository;

    @KafkaListener(topics = "game-topic", groupId = "dice-game-group")
    public void start(NotificationEvent event) {
        log.info(event.getData());
    }

    @KafkaListener(topics = "game-topic", groupId = "dice-game-group")
    public void end(NotificationEvent event) {
        log.info(event.getData());
    }

    @KafkaListener(topics = "game-prize-topic", groupId = "dice-game-group")
    public void prize(PrizeNotificationEvent event) {
        log.info(event.getData());
        this.saveData(event);
    }

    @Transactional
    public void saveData(PrizeNotificationEvent event) {
        //TODO need to fix
        Player player = playerRepository.getReferenceById(event.getPlayerId());
        List<Game> gameList = gameRepository.findByWinnerPlayer(player);
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findByGameIn(gameList);
        gamePlayerList.forEach(gamePlayer->gamePlayer.setScore(0));
        gamePlayerRepository.saveAll(gamePlayerList);
    }
}
