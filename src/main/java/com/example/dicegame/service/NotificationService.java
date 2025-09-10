package com.example.dicegame.service;

import com.example.dicegame.model.event.NotificationEvent;

public interface NotificationService {
    void send(NotificationEvent event);
}
