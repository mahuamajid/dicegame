package com.example.dicegame.service.impl;

import com.example.dicegame.client.support.RollDiceSupport;
import com.example.dicegame.constant.State;
import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.service.PlayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@Slf4j
public class PlayServiceImpl implements PlayService {
    private GameRepository gameRepository;
    private RollDiceSupport rollDiceSupport;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final AtomicBoolean engineRunning = new AtomicBoolean(false);

    PlayServiceImpl(GameRepository gameRepository, RollDiceSupport rollDiceSupport) {
        this.gameRepository = gameRepository;
        this.rollDiceSupport = rollDiceSupport;
    }

    @Override
    public void play(Game game) {
        runEngineAsync(game);
    }

    private void runEngineAsync(Game game) {
        if (engineRunning.get()) return;
        engineRunning.set(true);
        executor.submit(() -> engineLoop(game));
    }

    private void engineLoop(Game game) {
        try {
            int idx = 0;
            List<Player> players = new ArrayList<>(game.getPlayers());
            while (!game.isFinished()) {
                Player player = players.get(idx % players.size());
                tickTurn(game, player);
                Thread.sleep(300);// small delay to make /scores polling meaningful and keep CPU low
                idx++;
            }
        } catch (InterruptedException ignored) {
        } finally {
            engineRunning.set(false);
        }
    }

    private void tickTurn(Game game, Player player) {
        if (game.isFinished()) return;

        int roll = rollDiceSupport.roll();

        //TODO implement strategy pattern on state
        switch (player.getState()) {
            case BEFORE_START -> {
                if (roll == 6) {
                    logPlayersRollValue(player, roll);
                    player.setState(State.START_ROLL);
                    int startRoll = rollDiceSupport.roll();
                    logPlayersRollValue(player, startRoll);
                    if (startRoll == 6) {
                        // starting point 0
                    } else if (startRoll == 4) {
                        // special rule: no -4, but must roll another 6 to start accumulating points
                        player.setState(State.BEFORE_START);
                    } else {
                        player.setScore(player.getScore() + startRoll);
                        player.setState(State.ACTIVE);
                        checkWin(game, player);
                    }
                } else {
                    logPlayersRollValue(player, roll);
                }
            }
            case START_ROLL -> {
            // should not happen; handled immediately above
            }
            case ACTIVE -> {
                logPlayersRollValue(player, roll);
                if (roll == 6) {
                    player.setScore(player.getScore() + 6);
                    checkWin(game, player);
                    if (!game.isFinished()) {
                        int extra = rollDiceSupport.roll();
                        logPlayersRollValue(player, extra);
                        // extra roll is a normal roll
                        applyActiveRoll(game, player, extra);
                    }
                } else {
                    applyActiveRoll(game, player, roll);
                }
            }
        }
    }

    private void applyActiveRoll(Game game, Player player, int value) {
        if (value == 4) {
            player.setScore(Math.max(0, player.getScore() - 4));
            // stay ACTIVE per normal rule
        } else if (value >= 1 && value <= 6) {
            player.setScore(player.getScore() + value);
        }
        checkWin(game, player);
    }

    private void checkWin(Game game, Player player) {
        if (player.getScore() >= game.getTargetScore()) {
            game.setFinished(true);
            game.setWinnerPlayer(player);
            gameRepository.save(game);
        }
    }

    private void logPlayersRollValue(Player player, int value) {
        log.info("Player name:{}, Total Score:{}, Current Value of Dice:{}", player.getPlayerName(), player.getScore(), value);
    }
}
