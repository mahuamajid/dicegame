package com.example.dicegame.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * GamePlayer-related status codes and messages.
 */
@Getter
@RequiredArgsConstructor
public enum GamePlayerStatusDictionary {

    //Success code & messages
    GAME_PLAYER_FETCH_SUCCESS("901-003-0001-0", "Game fetched successfully"),
    GAME_PLAYER_SAVED_SUCCESS("901-003-0002-0", "Player saved in game  successfully"),
    GAME_PLAYER_UPDATED_SUCCESS("901-003-0003-0", "Game updated successfully"),
    GAME_PLAYER_PAGE_FETCH_SUCCESS("901-003-0004-0", "Game fetched successfully with pagination"),

    // Error code & messages

    GAME_PLAYER_FETCH_FAILED("901-003-2001-1", "Failed to fetch game");

    private final String statusCode;
    private final String message;
}