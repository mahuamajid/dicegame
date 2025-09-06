package com.example.dicegame.constant;

public class ErrorConstant {
    private ErrorConstant() {
    }

    public static final String PLAYER_NAME_REQUIRED = "Player name is required.";
    public static final String PLAYER_NAME_LENGTH = "Player name must be between 2 and 50 characters.";

    public static final String PLAYER_AGE_REQUIRED = "Age must not be null.";
    public static final String PLAYER_MIN_AGE = "Age must be at least 5.";
}
