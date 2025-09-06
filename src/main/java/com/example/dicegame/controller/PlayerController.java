package com.example.dicegame.controller;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerRequest;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.ApiResponse;
import com.example.dicegame.model.dto.response.base.ApiResponseFactory;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;
import com.example.dicegame.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.dicegame.constant.ApiRoutes.PLAYER;
import static com.example.dicegame.constant.PlayerStatusDictionary.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(PLAYER)
@Tag(name = "Player",
        description = "API endpoints for managing Players")
public class PlayerController {
    private final PlayerService playerService;
    private final ApiResponseFactory responseFactory;

    @Operation(summary = "Create a new Player",
            description = "Creates a new Player with the provided data")
    @PostMapping
    public ResponseEntity<ApiResponse<PlayerResponse>> create(@Valid @RequestBody PlayerRequest playerRequest)
            throws PlayerException {
        return responseFactory.create(playerService.createPlayer(playerRequest),
                PLAYER_SAVED_SUCCESS.getStatusCode(),
                PLAYER_SAVED_SUCCESS.getMessage());
    }

    @Operation(
            summary = "Retrieve Players with pagination",
            description = "Provides a list of Player with pagination with the provided data available in the system"
    )
    @PostMapping("/page-search")
    public ResponseEntity<ApiResponse<PaginatedResponse<PlayerResponse>>> pageData(@RequestBody PlayerSearchRequest playerSearchRequest) throws PlayerException {
        return responseFactory.success(playerService.pagePlayer(playerSearchRequest),
                PLAYER_PAGE_FETCH_SUCCESS.getStatusCode(),
                PLAYER_PAGE_FETCH_SUCCESS.getMessage());
    }

    @Operation(
            summary = "Retrieve all Players",
            description = "Provides a list of Player with the provided data available in the system"
    )
    @GetMapping("/get/all/game-id/{gameId}")
    public ResponseEntity<ApiResponse<List<PlayerResponse>>> list(@PathVariable("gameId") Integer gameId) {
        return responseFactory.success(playerService.playerList(gameId),
                PLAYER_FETCH_SUCCESS.getStatusCode(),
                PLAYER_FETCH_SUCCESS.getMessage());
    }
}
