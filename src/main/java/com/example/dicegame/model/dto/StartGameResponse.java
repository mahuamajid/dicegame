package com.example.dicegame.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StartGameResponse {
    private boolean started;
    private int targetScore;
}
