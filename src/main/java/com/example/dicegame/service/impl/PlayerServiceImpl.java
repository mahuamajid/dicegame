package com.example.dicegame.service.impl;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.dicegame.constant.PlayerStatusDictionary.PLAYER_REQUEST_NULL;
import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;

    @Override
    public PlayerResponse createPlayer(PlayerRequest playerRequest) throws PlayerException {
        return Optional.ofNullable(playerRequest)
                .map(req -> {
                    Player p = playerRepository.save(mapObject(req, Player.class));
                    return mapObject(p, PlayerResponse.class);
                })
                .orElseThrow(()-> new PlayerException(PLAYER_REQUEST_NULL.getMessage(),
                        PLAYER_REQUEST_NULL.getStatusCode()));
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
