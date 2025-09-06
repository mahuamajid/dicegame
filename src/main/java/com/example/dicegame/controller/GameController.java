package com.example.dicegame.controller;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.GameRequest;
import com.example.dicegame.model.dto.response.GameResponse;
import com.example.dicegame.model.dto.response.StartGameResponse;
import com.example.dicegame.model.dto.response.base.ApiResponse;
import com.example.dicegame.model.dto.response.base.ApiResponseFactory;
import com.example.dicegame.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.dicegame.constant.ApiRoutes.GAME;
import static com.example.dicegame.constant.GameStatusDictionary.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(GAME)
@Tag(name = "Game",
        description = "API endpoints for managing Games")
public class GameController {
    private final GameService gameService;
    private final ApiResponseFactory responseFactory;

    @Operation(summary = "Create a new Game",
            description = "Creates new Game with the provided data")
    @PostMapping
    public ResponseEntity<ApiResponse<GameResponse>> create(@Valid @RequestBody GameRequest gameRequest) {
        return responseFactory.create(gameService.create(gameRequest),
                GAME_SAVED_SUCCESS.getStatusCode(),
                GAME_SAVED_SUCCESS.getMessage());
    }

    @Operation(summary = "Start a new Game",
            description = "Starts new Game with the provided data")
    @PutMapping("/start/target-value/{targetValue}")
    public ResponseEntity<ApiResponse<StartGameResponse>> start(@PathVariable(value = "targetValue", required = false) String targetValue) {
        return responseFactory.create(gameService.start(targetValue),
                GAME_UPDATED_SUCCESS.getStatusCode(),
                GAME_UPDATED_SUCCESS.getMessage());
    }

    @Operation(
            summary = "Retrieve score a Game",
            description = "Retrieve scores a Game"
    )
    @GetMapping("/score")
    public ResponseEntity<ApiResponse<GameResponse>> score() {
        return responseFactory.success(gameService.score(),
                GAME_FETCH_SUCCESS.getStatusCode(),
                GAME_FETCH_SUCCESS.getMessage());
    }

    @Operation(
            summary = "Retrieve status of a Game",
            description = "Retrieve status of a Game"
    )
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<GameResponse>> status() {
        return responseFactory.success(gameService.status(),
                GAME_FETCH_SUCCESS.getStatusCode(),
                GAME_FETCH_SUCCESS.getMessage());
    }
}
