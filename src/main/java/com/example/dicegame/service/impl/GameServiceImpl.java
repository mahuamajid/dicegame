package com.example.dicegame.service.impl;

import com.example.dicegame.config.AppProperties;
import com.example.dicegame.exception.GameException;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.model.entity.PlayerGame;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.repository.PlayerGameRepository;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.dicegame.constant.GameStatusDictionary.*;
import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerGameRepository playerGameRepository;
    private final AppProperties appProperties;

    @Transactional
    @Override
    public StartGameResponse start(GameRequest gameRequest) throws GameException {
        Game game = createGame(gameRequest);
        addPlayerInGame(game, gameRequest);
        Game startedGame = startGame(game);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse score(Integer gameId) throws GameException {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> {
            log.error("Game not found by id {}", gameId);
            return new GameException(GAME_FETCH_FAILED.getMessage(), GAME_FETCH_FAILED.getStatusCode());
        });
        List<PlayerGame> playerGameList = playerGameRepository.findByGameId(gameId);
        List<PlayerResponse> playerResponseList = playerGameList.stream()
                .map(playerGame -> mapObject(playerGame.getPlayer(), PlayerResponse.class))
                .toList();
        GameResponse gameResponse = mapObject(game, GameResponse.class);
        gameResponse.setPlayerResponseList(playerResponseList);
        return gameResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse status(Integer gameId) throws GameException {
        return score(gameId);
    }

    private Game startGame(Game game) throws GameException {
        if (game.isStarted()) {
            log.error("Game already started");
            throw new GameException(GAME_ALREADY_STARTED.getMessage(), GAME_ALREADY_STARTED.getStatusCode());
        }
        game.setStarted(Boolean.TRUE);
        return gameRepository.save(game);
    }

    public void addPlayerInGame(Game game, GameRequest gameRequest) throws GameException {
        if (gameRequest.getPlayerIdList().size() < 2 || gameRequest.getPlayerIdList().size() > 4) {
            log.error("Game request parameters error. Need 2 to 4 players to play");
            throw new GameException(GAME_INVALID_PLAYER_NUMBER.getMessage(), GAME_INVALID_PLAYER_NUMBER.getStatusCode());
        }
        List<Player> playerList = playerRepository.findByIdIn(gameRequest.getPlayerIdList());
        playerList.forEach(player -> {
            playerGameRepository.save(PlayerGame.builder()
                    .game(game)
                    .player(player)
                    .build());
        });
        game.setPlayerList(playerList);
    }

    public Game createGame(GameRequest gameRequest) {
        Game game = gameRepository.findByGameName(gameRequest.getGameName())
                .orElse(null);
        if (Objects.nonNull(game)) {
            return game;
        }
        return gameRepository.save(Game.builder()
                .gameName(gameRequest.getGameName())
                .targetScore(appProperties.getTargetScore())
                .build());
    }

    public void reset(Integer gameId) {
        //TODO parameter could be game here
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        playerGameRepository.findByGameId(gameId).forEach(playerGameRepository::delete);
        game.setStarted(false);
        game.setFinished(false);
        game.setTargetScore(appProperties.getTargetScore());
        game.setWinnerPlayer(null);
        gameRepository.save(game);
    }
}
