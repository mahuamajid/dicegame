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
    GAME_START_SUCCESS("901-002-0005-0", "Game started successfully"),

    // Error code & messages

    GAME_FETCH_FAILED("901-002-2001-1", "Failed to fetch game"),
    GAME_ALREADY_STARTED("901-002-2002-1", "Game already started"),
    GAME_INVALID_PLAYER_NUMBER("901-002-2003-1", "Need 2 to 4 players to play");

    private final String statusCode;
    private final String message;
}