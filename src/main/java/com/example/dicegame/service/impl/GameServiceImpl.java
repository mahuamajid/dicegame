package com.example.dicegame.service.impl;

import com.example.dicegame.config.AppProperties;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import com.example.dicegame.model.entity.Game;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final AppProperties appProperties;

    @Override
    public GameResponse create(GameRequest gameRequest) {
        Game game = Game.builder()
                .gameName(gameRequest.getGameName())
                .targetScore(appProperties.getTargetScore())
                .build();
        return mapObject(gameRepository.save(game), GameResponse.class);
    }

    @Override
    public StartGameResponse start(String targetValue) {
        return null;
    }

    @Override
    public GameResponse score() {
        return null;
    }

    @Override
    public GameResponse status() {
        return null;
    }
}
