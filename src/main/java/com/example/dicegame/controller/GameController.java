package com.example.dicegame.controller;

import com.example.dicegame.exception.GameException;
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

    @Operation(summary = "Start a new Game",
            description = "Starts new Game with the provided data")
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<StartGameResponse>> start(@RequestBody @Valid GameRequest gameRequest)
            throws GameException {
        StartGameResponse started = gameService.start(gameRequest);
        if(started.isStarted()){
            return responseFactory.create(started,
                    GAME_START_SUCCESS.getStatusCode(),
                    GAME_START_SUCCESS.getMessage());
        } else {
            return responseFactory.create(started,
                    GAME_NOT_START.getStatusCode(),
                    GAME_NOT_START.getMessage());
        }
    }

    @Operation(
            summary = "Retrieve score a Game",
            description = "Retrieve scores a Game"
    )
    @GetMapping("/score/{gameId}")
    public ResponseEntity<ApiResponse<GameResponse>> score(@PathVariable("gameId") Integer gameId)
            throws GameException {
        return responseFactory.success(gameService.score(gameId),
                GAME_FETCH_SUCCESS.getStatusCode(),
                GAME_FETCH_SUCCESS.getMessage());
    }

    @Operation(
            summary = "Retrieve status of a Game",
            description = "Retrieve status of a Game"
    )
    @GetMapping("/status/{gameId}")
    public ResponseEntity<ApiResponse<GameResponse>> status(@PathVariable("gameId")Integer gameId)
            throws GameException {
        return responseFactory.success(gameService.status(gameId),
                GAME_FETCH_SUCCESS.getStatusCode(),
                GAME_FETCH_SUCCESS.getMessage());
    }
}
