package com.example.dicegame.exception;

import lombok.Getter;

@Getter
public class GameException extends Exception {
    private final String statusCode;

    public GameException(String message, String statusCode) {
        super(message);
        this.statusCode= statusCode;
    }
}