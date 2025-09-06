package com.example.dicegame.exception;

import lombok.Getter;

@Getter
public class PlayerException extends Exception {
    private final String statusCode;

    public PlayerException(String message, String statusCode) {
        super(message);
        this.statusCode= statusCode;
    }
}