package com.example.dicegame.model.dto.response;

import com.example.dicegame.constant.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerResponse {
    private Integer id;
    private String playerName;
    private int age;
    @Enumerated(EnumType.STRING)
    private State state;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "UTC")
    private Instant createTime;
}
