package com.example.dicegame.service;

import com.example.dicegame.model.event.NotificationEvent;
import com.example.dicegame.model.event.PrizeNotificationEvent;

public interface NotificationService {
    void send(NotificationEvent event);
    void sendForPrize(PrizeNotificationEvent event);
}
