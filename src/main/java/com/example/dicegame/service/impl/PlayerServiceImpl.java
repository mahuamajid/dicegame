package com.example.dicegame.service.impl;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;
import com.example.dicegame.model.entity.Player;
import com.example.dicegame.repository.PlayerCustomRepository;
import com.example.dicegame.repository.PlayerRepository;
import com.example.dicegame.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static com.example.dicegame.constant.PlayerStatusDictionary.PLAYER_REQUEST_NULL;
import static com.example.dicegame.util.ObjectUtil.mapObject;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final PlayerCustomRepository playerCustomRepository;

    @Transactional
    @Override
    public PlayerResponse createPlayer(PlayerRequest playerRequest) throws PlayerException {
        return Optional.ofNullable(playerRequest)
                .map(req -> {
                   Player player = mapObject(req, Player.class);
                    player.setCreateTime(Instant.now());
                    return mapObject(playerRepository.save(player), PlayerResponse.class);
                })
                .orElseThrow(()-> new PlayerException(PLAYER_REQUEST_NULL.getMessage(),
                        PLAYER_REQUEST_NULL.getStatusCode()));
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedResponse<PlayerResponse> pagePlayer(PlayerSearchRequest playerSearchRequest) throws PlayerException {
        return playerCustomRepository.pageData(playerSearchRequest);
    }
}
