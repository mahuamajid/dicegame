package com.example.dicegame.service;

import com.example.dicegame.exception.GameException;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;

public interface GameService {
    StartGameResponse start(GameRequest gameRequest) throws GameException;
    GameResponse score(Integer gameId) throws GameException;
    GameResponse status(Integer gameId) throws GameException;
}
