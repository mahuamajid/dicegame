package com.example.dicegame.service;

import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;

public interface GameService {
    StartGameResponse start(GameRequest gameRequest);
    GameResponse score(Integer gameId);
    GameResponse status(Integer gameId);
}
