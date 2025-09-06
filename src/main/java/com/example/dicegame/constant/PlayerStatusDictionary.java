package com.example.dicegame.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Player-related status codes and messages.
 */
@Getter
@RequiredArgsConstructor
public enum PlayerStatusDictionary {

    //Success code & messages
    PLAYER_FETCH_SUCCESS("901-001-0001-0", "Player fetched successfully"),
    PLAYER_SAVED_SUCCESS("901-001-0002-0", "Player saved successfully"),
    PLAYER_PAGE_FETCH_SUCCESS("901-001-0003-0", "Player fetched successfully with pagination"),

    // Error code & messages

    PLAYER_FETCH_FAILED("901-001-2001-1", "Failed to fetch player"),
    PLAYER_REQUEST_NULL("901-001-2002-1", "PlayerRequest cannot be null");

    private final String statusCode;
    private final String message;
}