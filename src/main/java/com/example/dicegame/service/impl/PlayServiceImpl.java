package com.example.dicegame.service.impl;

import com.example.dicegame.client.support.RollDiceSupport;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.GamePlayer;
import com.example.dicegame.repository.GamePlayerRepository;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.dicegame.constant.AppConstant.GAME_KAY;
import static com.example.dicegame.constant.State.*;
import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final ConcurrentMap<Integer, AtomicBoolean> gameLocks = new ConcurrentHashMap<>();

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final RollDiceSupport rollDiceSupport;
    private final RedisTemplate<String, Object> redisTemplate;

    PlayServiceImpl(GameRepository gameRepository, PlayerRepository playerRepository, GamePlayerRepository gamePlayerRepository,
                    RollDiceSupport rollDiceSupport, RedisTemplate<String, Object> redisTemplate) {
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.rollDiceSupport = rollDiceSupport;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void play(Game game) {
        if(!game.isStarted()) {
            return;
        }
        List<GamePlayer> gamePlayerList = gamePlayerRepository.findByGameId(game.getId());
        runEngineAsync(game, gamePlayerList);
    }

    private void runEngineAsync(Game game, List<GamePlayer> gamePlayerList) {
        AtomicBoolean running = gameLocks.computeIfAbsent(game.getId(), id -> new AtomicBoolean(false));
        if (!running.compareAndSet(false, true)) {
            log.info("Game {} is already running", game.getId());
            return;
        }
        executor.submit(() -> {
            try {
                engineLoop(game, gamePlayerList);
            } finally {
                game.getPlayers().forEach(gamePlayer -> gamePlayer.setState(AVAILABLE));
                playerRepository.saveAll(game.getPlayers());
                running.set(false);
                gameLocks.remove(game.getId());
            }
        });
    }

    private void engineLoop(Game game, List<GamePlayer> gamePlayerList) {
        try {
            int idx = 0;
            while (!game.isFinished()) {
                GamePlayer gamePlayer = gamePlayerList.get(idx % gamePlayerList.size());
                tickTurn(game, gamePlayer);
                Thread.sleep(300);// small delay to make /scores polling meaningful and keep CPU low
                idx++;
            }
            gamePlayerRepository.saveAll(gamePlayerList);
        } catch (InterruptedException ignored) {

        }
    }

    private void tickTurn(Game game, GamePlayer gamePlayer) {
        if (game.isFinished()) return;

        int value = rollDiceSupport.roll();

        switch (gamePlayer.getPlayer().getState()) {
            case BEFORE_START -> {
                if (value == 6) {
                    logPlayersRollValue(gamePlayer, value);
                    gamePlayer.getPlayer().setState(START_ROLL);
                    int startRoll = rollDiceSupport.roll();
                    logPlayersRollValue(gamePlayer, startRoll);
                    if (startRoll == 6) {
                        // starting point 0
                    } else if (startRoll == 4) {
                        // special rule: no -4, but must roll another 6 to start accumulating points
                        gamePlayer.getPlayer().setState(BEFORE_START);
                    } else {
                        gamePlayer.setScore(gamePlayer.getScore() + startRoll);
                        gamePlayer.getPlayer().setState(ACTIVE);
                        checkWin(game, gamePlayer);
                    }
                } else {
                    logPlayersRollValue(gamePlayer, value);
                }
            }
            case START_ROLL -> {
                // should not happen; handled immediately above
            }
            case ACTIVE -> {
                logPlayersRollValue(gamePlayer, value);
                if (value == 6) {
                    gamePlayer.setScore(gamePlayer.getScore() + 6);
                    checkWin(game, gamePlayer);
                    if (!game.isFinished()) {
                        int extra = rollDiceSupport.roll();
                        logPlayersRollValue(gamePlayer, extra);
                        // extra roll is a normal roll
                        applyActiveRoll(game, gamePlayer, extra);
                    }
                } else {
                    applyActiveRoll(game, gamePlayer, value);
                }
            }
        }
    }

    private void applyActiveRoll(Game game, GamePlayer gamePlayer, int value) {
        if (value == 4) {
            gamePlayer.setScore(Math.max(0, gamePlayer.getScore() - 4));
            // stay ACTIVE per normal rule
        } else if (value >= 1 && value <= 6) {
            gamePlayer.setScore(gamePlayer.getScore() + value);
        }
        checkWin(game, gamePlayer);
    }

    private void checkWin(Game game, GamePlayer gamePlayer) {
        if (gamePlayer.getScore() >= game.getTargetScore()) {
            game.setFinished(true);
            game.setWinnerPlayer(gamePlayer.getPlayer());
            gameRepository.save(game);
            redisTemplate.opsForValue().set(GAME_KAY + game.getId(), mapObject(gamePlayer.getPlayer(), PlayerResponse.class));
        }
    }

    private void logPlayersRollValue(GamePlayer gamePlayer, int value) {
        log.info("Player name:{}, Total Score:{}, Current Value of Dice:{}", gamePlayer.getPlayer().getPlayerName(),
                gamePlayer.getScore(), value);
    }
}
