package com.example.dicegame.repository;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;

public interface PlayerCustomRepository {
    PaginatedResponse<PlayerResponse> pageData(PlayerSearchRequest playerSearchRequest) throws PlayerException;
}
