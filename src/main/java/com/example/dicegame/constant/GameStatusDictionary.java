package com.example.dicegame.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Game-related status codes and messages.
 */
@Getter
@RequiredArgsConstructor
public enum GameStatusDictionary {

    //Success code & messages
    GAME_FETCH_SUCCESS("901-002-0001-0", "Game fetched successfully"),
    GAME_SAVED_SUCCESS("901-002-0002-0", "Game saved successfully"),
    GAME_UPDATED_SUCCESS("901-002-0003-0", "Game updated successfully"),
    GAME_PAGE_FETCH_SUCCESS("901-002-0004-0", "Game fetched successfully with pagination"),

    // Error code & messages

    GAME_FETCH_FAILED("901-002-2001-1", "Failed to fetch game");

    private final String statusCode;
    private final String message;
}