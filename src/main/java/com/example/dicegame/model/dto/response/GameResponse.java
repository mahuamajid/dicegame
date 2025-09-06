package com.example.dicegame.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {
    private boolean started;
    private boolean finished;
    private int targetScore;
    private Integer winnerPlayerId;
    private List<PlayerResponse> playerResponseList;
}
