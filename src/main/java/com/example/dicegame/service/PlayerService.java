package com.example.dicegame.service;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;

public interface PlayerService {
    PlayerResponse createPlayer(PlayerRequest playerRequest) throws PlayerException;
    PaginatedResponse<PlayerResponse> pagePlayer(PlayerSearchRequest playerSearchRequest) throws PlayerException;
}
