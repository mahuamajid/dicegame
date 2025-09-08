package com.example.dicegame.model.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponse {
    private String gameName;
    private boolean started;
    private boolean finished;
    private int targetScore;
    private PlayerResponse winnerPlayer;
    private List<ScoreResponse> scoreResponseList;
}
