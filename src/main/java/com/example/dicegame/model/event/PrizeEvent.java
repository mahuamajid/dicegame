package com.example.dicegame.model.event;

import com.example.dicegame.constant.GameStateType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrizeEvent implements Serializable {
    private String gameName;
    private String data;
    private GameStateType gameStateType;
    private long timestamp;
}
