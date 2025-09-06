package com.example.dicegame.service.impl;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponse createPlayer(PlayerRequest playerRequest) {
        return null;
    }

    @Override
    public List<PlayerResponse> playerList() {
        return List.of();
    }

    @Override
    public PaginatedResponse<PlayerResponse> pagePlayer(PlayerSearchRequest playerSearchRequest) throws PlayerException {
        return null;
    }
}
