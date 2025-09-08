package com.example.dicegame.service;

import com.example.dicegame.exception.GameException;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;

import java.util.List;

public interface GameService {
    StartGameResponse start(GameRequest gameRequest) throws GameException;
    List<PlayerResponse> playerList(Integer gameId);
    GameResponse score(Integer gameId) throws GameException;
    PlayerResponse winner(Integer gameId) throws GameException;
}
