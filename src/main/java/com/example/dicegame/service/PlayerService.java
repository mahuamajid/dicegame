package com.example.dicegame.service;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;

import java.util.List;

public interface PlayerService {
    PlayerResponse createPlayer(PlayerRequest playerRequest);
    List<PlayerResponse> playerList();
    PaginatedResponse<PlayerResponse> pagePlayer(PlayerSearchRequest playerSearchRequest) throws PlayerException;
}
