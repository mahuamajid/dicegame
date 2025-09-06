package com.example.dicegame.service.impl;

import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import com.example.dicegame.repository.GameRepository;
import com.example.dicegame.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Override
    public GameResponse create(GameRequest gameRequest) {
        return null;
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
