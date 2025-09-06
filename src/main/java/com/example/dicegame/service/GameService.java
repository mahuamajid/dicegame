package com.example.dicegame.service;

import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import jakarta.validation.Valid;

public interface GameService {
    GameResponse create(GameRequest gameRequest);

    StartGameResponse start(String targetValue);
    GameResponse score();
    GameResponse status();
}
