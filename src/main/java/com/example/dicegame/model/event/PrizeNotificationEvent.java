package com.example.dicegame.model.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PrizeNotificationEvent  extends NotificationEvent {
    private Integer playerId;
}
