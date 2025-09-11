package com.example.dicegame.service.impl;

import com.example.dicegame.config.AppConfig;
import com.example.dicegame.model.enums.State;
import com.example.dicegame.exception.GameException;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.ScoreResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.GamePlayer;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.model.event.NotificationEvent;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.repository.GamePlayerRepository;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.GameService;
import com.example.dicegame.service.NotificationService;
import com.example.dicegame.service.PlayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.dicegame.constant.AppConstant.GAME_KAY;
import static com.example.dicegame.constant.AppConstant.GAME_LOCK_KEY;
import static com.example.dicegame.model.enums.GameStateType.STARTED;
import static com.example.dicegame.constant.GameStatusDictionary.*;
import static com.example.dicegame.model.template.NotificationTemplate.gameStartTemplate;
import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final AppConfig appProperties;
    private final PlayService playService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper radisObjectMapper;
    private final RedissonClient redissonClient;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public StartGameResponse start(GameRequest gameRequest) throws GameException {
        Game game = createGame(gameRequest);
        addPlayerInGame(game, gameRequest);
        startGame(game);
        playService.play(game);
        return StartGameResponse.builder()
                .id(game.getId())
                .started(game.isStarted())
                .targetScore(game.getTargetScore())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PlayerResponse> playerList(Integer gameId) {
        List<GamePlayer> gamePlayerList = getGamePlayer(gameId);
        return gamePlayerList.stream()
                .map(gamePlayer -> mapObject(gamePlayer.getPlayer(), PlayerResponse.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse score(Integer gameId) throws GameException {
        Game game = getById(gameId);
        List<GamePlayer> gamePlayerList = getGamePlayer(gameId);
        Map<Integer, ScoreResponse> scoreResponseMap = gamePlayerList.stream()
                .map(gamePlayer -> {
                    return ScoreResponse.builder()
                            .playerResponse(mapObject(gamePlayer.getPlayer(), PlayerResponse.class))
                            .score(gamePlayer.getScore())
                            .build();
                }).collect(Collectors.toMap(scoreResponse -> scoreResponse.getPlayerResponse().getId(),
                        scoreResponse -> scoreResponse));
        GameResponse gameResponse = mapObject(game, GameResponse.class);
        Optional.ofNullable(game.getWinnerPlayer())
                .ifPresent(player -> gameResponse.setWinnerPlayer(scoreResponseMap.get(player.getId()).getPlayerResponse()));
        gameResponse.setScoreResponseList(scoreResponseMap.values().stream().toList());
        return gameResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public PlayerResponse winner(Integer gameId) throws GameException {
        LinkedHashMap redisData = (LinkedHashMap) redisTemplate.opsForValue().get(GAME_KAY + gameId);
        if (Objects.nonNull(redisData)) {
            return radisObjectMapper.convertValue(redisData, PlayerResponse.class);
        }
        Game game = getById(gameId);
        if (Objects.nonNull(game.getWinnerPlayer())) {
            return mapObject(game.getWinnerPlayer(), PlayerResponse.class);
        }
        throw new GameException(GAME_NOT_FINISHED.getMessage(), GAME_NOT_FINISHED.getStatusCode());
    }

    public void startGame(Game game) throws GameException {
        if (game.isStarted()) {
            log.error("Game already started");
            throw new GameException(GAME_ALREADY_STARTED.getMessage(), GAME_ALREADY_STARTED.getStatusCode());
        }
        List<GamePlayer> gamePlayerList = getGamePlayer(game.getId());
        if (gamePlayerList.size() >= 2 && gamePlayerList.size() <= 4) {
            game.setStarted(Boolean.TRUE);
            gameRepository.save(game);
        }
        NotificationEvent event = NotificationEvent.builder()
                .gameName(game.getGameName())
                .data(gameStartTemplate(game.getGameName()))
                .gameStateType(STARTED)
                .timestamp(System.currentTimeMillis())
                .build();
        notificationService.send(event);
    }

    public List<GamePlayer> getGamePlayer(Integer gameId) {
        return gamePlayerRepository.findByGameId(gameId);
    }

    public void addPlayerInGame(Game game, GameRequest gameRequest) throws GameException {
        if (game.isStarted()) {
            log.info("Game already started");
            return;
        }
        if (CollectionUtils.isEmpty(game.getPlayers())) {
            saveDataInGamePlayer(game, gameRequest.getPlayerIds());
            return;
        }
        if (game.getPlayers().size() >= 4) {
            log.info("There are already 4 players ready to play.");
            return;
        }
        Set<Integer> newPlayerIds = validatePlayerListForGame(game, gameRequest.getPlayerIds());
        saveDataInGamePlayer(game, newPlayerIds);
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

    public Game getById(Integer gameId) throws GameException {
        return gameRepository.findById(gameId).orElseThrow(() -> {
            log.error("Game not found by id {}", gameId);
            return new GameException(GAME_FETCH_FAILED.getMessage(), GAME_FETCH_FAILED.getStatusCode());
        });
    }

    public void saveDataInGamePlayer(Game game, Set<Integer> playerIds) throws GameException {
        RLock lock = redissonClient.getLock(GAME_LOCK_KEY + game.getId());
        boolean acquired = false;
        try {
            acquired = lock.tryLock(5, 0, TimeUnit.SECONDS); // wait up to 5s, lease 30s (watchdog auto-extends if lease <= 0)
            if (!acquired) {
                log.error("Could not acquire game lock");
                throw new GameException(GAME_LOCK_NOT_ACQUIRED.getMessage(), GAME_LOCK_NOT_ACQUIRED.getStatusCode());
            }
            log.info("Acquire game lock");
            List<Player> playerList = playerRepository.findByIdInAndState(playerIds.stream().toList(), State.AVAILABLE);
            if(CollectionUtils.isEmpty(playerList)) {
                throw new GameException(INVALID_PLAYER_ID.getMessage(), INVALID_PLAYER_ID.getStatusCode());
            }
            playerList.forEach(player -> {
                player.setState(State.BEFORE_START);
                playerRepository.save(player);
                gamePlayerRepository.save(GamePlayer.builder()
                        .game(game)
                        .player(player)
                        .build());
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while waiting to add player to the game", e);
            throw new GameException(GAME_LOCK_INTERRUPTED.getMessage(), GAME_LOCK_INTERRUPTED.getStatusCode());
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private Set<Integer> validatePlayerListForGame(Game game, Set<Integer> newPlayerIds) throws GameException {
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
        return newPlayerIds;
    }
}
