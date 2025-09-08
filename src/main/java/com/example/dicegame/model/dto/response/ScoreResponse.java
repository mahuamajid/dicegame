package com.example.dicegame.model.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreResponse {
    private PlayerResponse playerResponse;
    private Integer score;
}
