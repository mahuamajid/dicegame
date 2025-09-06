package com.example.dicegame.model.dto.response;

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
