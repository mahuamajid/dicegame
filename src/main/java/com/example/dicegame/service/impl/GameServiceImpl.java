package com.example.dicegame.service.impl;

import com.example.dicegame.config.AppProperties;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final PlayerGameRepository playerGameRepository;
    private final AppProperties appProperties;

    @Transactional
    @Override
    public StartGameResponse start(GameRequest gameRequest) {
        Game game = createGame(gameRequest);
        GameResponse gameResponse = addPlayerInGame(game, gameRequest);
        startGame(game);
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public GameResponse score(Integer gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(()-> new RuntimeException("Game not found"));
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
    public GameResponse status(Integer gameId) {
        return null;
    }

    private Game startGame(Game game) {
        if(game.isStarted()){
            throw new RuntimeException("Game already started");
        }
        game.setStarted(Boolean.TRUE);
        return gameRepository.save(game);
    }

    public GameResponse addPlayerInGame(Game game, GameRequest gameRequest) {
        if(gameRequest.getPlayerIdList().size()<2 || gameRequest.getPlayerIdList().size()>4){
            throw new RuntimeException("Need 2 to 4 players to play");
        }
        List<Player> playerList = playerRepository.findByIdIn(gameRequest.getPlayerIdList());
        List<PlayerResponse> playerResponseList = new ArrayList<>();
        playerList.forEach(player -> {
            playerGameRepository.save(PlayerGame.builder()
                    .game(game)
                    .player(player)
                    .build());
            playerResponseList.add(mapObject(player, PlayerResponse.class));
        });
        GameResponse gameResponse = mapObject(game, GameResponse.class);
        gameResponse.setPlayerResponseList(playerResponseList);
        return gameResponse;
    }

    public Game createGame(GameRequest gameRequest) {
        Game game = gameRepository.findByGameName(gameRequest.getGameName())
                .orElseThrow(()-> new RuntimeException("Game not found"));
        if(Objects.nonNull(game)){
            return game;
        }
        return gameRepository.save(Game.builder()
                .gameName(gameRequest.getGameName())
                .targetScore(appProperties.getTargetScore())
                .build());
    }

    public void reset(Integer gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(()-> new RuntimeException("Game not found"));
        playerGameRepository.findByGameId(gameId).forEach(playerGameRepository::delete);
        game.setStarted(false);
        game.setFinished(false);
        game.setTargetScore(appProperties.getTargetScore());
        game.setWinnerPlayer(null);
        gameRepository.save(game);
    }
}
