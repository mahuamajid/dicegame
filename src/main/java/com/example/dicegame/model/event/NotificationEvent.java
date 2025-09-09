package com.example.dicegame.model.event;

import com.example.dicegame.model.enums.GameStateType;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class NotificationEvent implements Serializable {
    protected String gameName;
    protected String data;
    protected GameStateType gameStateType;
    protected long timestamp;
}
