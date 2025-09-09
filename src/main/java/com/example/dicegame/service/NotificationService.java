package com.example.dicegame.service;

import com.example.dicegame.constant.GameStateType;

public interface NotificationService {
    void send(String gameName, String data, GameStateType gameStateType);
}
