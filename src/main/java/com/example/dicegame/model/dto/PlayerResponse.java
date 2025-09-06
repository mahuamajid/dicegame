package com.example.dicegame.model.dto;

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

    private String name;

    private int age;

    private int score;

    @Enumerated(EnumType.STRING)
    private String state;
}
