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
    GAME_SCORE_FETCH_SUCCESS("901-002-0001-0", "Game score fetched successfully"),
    GAME_WINNER_FETCH_SUCCESS("901-002-0002-0", "Game winner fetched successfully"),
    GAME_PLAYER_FETCH_SUCCESS("901-002-0003-0", "Game player fetched successfully"),
    GAME_START_SUCCESS("901-002-0004-0", "Game started successfully"),
    GAME_NOT_START("901-002-0005-0", "Game created successfully"),

    // Error code & messages

    GAME_FETCH_FAILED("901-002-2001-1", "Failed to fetch game"),
    GAME_ALREADY_STARTED("901-002-2002-1", "Game already started"),
    GAME_INVALID_PLAYER_NUMBER("901-002-2003-1", "Need 2 to 4 players to play"),
    GAME_NOT_FINISHED("901-002-2004-1", "Game not finished yet"),
    GAME_LOCK_NOT_ACQUIRED("901-002-2005-1", "Could not acquire game lock"),
    GAME_LOCK_INTERRUPTED("901-002-2006-1", "Interrupted while waiting to add player to the game"),
    INVALID_PLAYER_ID("901-002-2007-1", "Invalid players id"),;

    private final String statusCode;
    private final String message;
}