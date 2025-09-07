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

import java.util.*;
import java.util.stream.Collectors;

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
    private final PlayService playService;

    @Transactional
    @Override
    public StartGameResponse start(GameRequest gameRequest) throws GameException {
        Game game = createGame(gameRequest);
        addPlayerInGame(game, gameRequest);
        Game startedGame = startGame(game);
        playService.play(startedGame);
        return StartGameResponse.builder()
                .started(startedGame.isStarted())
                .targetScore(startedGame.getTargetScore())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse score(Integer gameId) throws GameException {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> {
            log.error("Game not found by id {}", gameId);
            return new GameException(GAME_FETCH_FAILED.getMessage(), GAME_FETCH_FAILED.getStatusCode());
        });
        List<PlayerGame> playerGameList = playerGameRepository.findByGameId(gameId);
        Map<Integer,PlayerResponse> playerResponseMap = playerGameList.stream()
                .map(playerGame -> mapObject(playerGame.getPlayer(), PlayerResponse.class))
                .collect(Collectors.toMap(PlayerResponse::getId, playerResponse -> playerResponse));
        GameResponse gameResponse = mapObject(game, GameResponse.class);
        Optional.ofNullable(game.getWinnerPlayer()).ifPresent(player ->  gameResponse.setWinnerPlayer(playerResponseMap.get(player.getId())));
        gameResponse.setPlayerResponseList(playerResponseMap.values().stream().toList());
        return gameResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse status(Integer gameId) throws GameException {
        return score(gameId);
    }

    public Game startGame(Game game) throws GameException {
        if (game.isStarted()) {
            log.error("Game already started");
            throw new GameException(GAME_ALREADY_STARTED.getMessage(), GAME_ALREADY_STARTED.getStatusCode());
        }
        if (game.getPlayers().size() >= 2 && game.getPlayers().size() <= 4) {
            game.setStarted(Boolean.TRUE);
            return gameRepository.save(game);
        }
        return game;
    }

    public void addPlayerInGame(Game game, GameRequest gameRequest) throws GameException {
        if (game.isStarted()) {
            log.info("Game already started");
            return;
        }
        if (game.getPlayers().size() >= 4) {
            log.info("There are already 4 players ready to play.");
            return;
        }
        List<Integer> newPlayerIdList = validatePlayerListForGame(game, gameRequest.getPlayerIds());
        List<Player> playerList = playerRepository.findByIdIn(newPlayerIdList);
        Set<Player> players = playerList.stream().peek(player ->
                playerGameRepository.save(PlayerGame.builder()
                        .game(game)
                        .player(player)
                        .build())).collect(Collectors.toSet());
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

    private List<Integer> validatePlayerListForGame(Game game, Set<Integer> newPlayerIds) throws GameException {
        Set<Integer> existingPlayerIds = game.getPlayers().stream().map(Player::getId).collect(Collectors.toSet());
        newPlayerIds.addAll(existingPlayerIds);
        if (newPlayerIds.size() > 4) {
            log.error("Invalid player number");
            throw new GameException(GAME_INVALID_PLAYER_NUMBER.getMessage(), GAME_INVALID_PLAYER_NUMBER.getStatusCode());
        }
        newPlayerIds.removeAll(existingPlayerIds);
        if (newPlayerIds.isEmpty()) {
            log.info("No player to add");
        }
        return newPlayerIds.stream().toList();
    }
}
