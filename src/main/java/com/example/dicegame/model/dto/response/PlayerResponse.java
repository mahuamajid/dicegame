package com.example.dicegame.model.dto.response;

import com.example.dicegame.constant.State;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerResponse {
    private Integer id;

    private String playerName;

    private int age;

    private int score;

    @Enumerated(EnumType.STRING)
    private State state;
}
