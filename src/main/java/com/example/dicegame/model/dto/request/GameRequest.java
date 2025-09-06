package com.example.dicegame.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.example.dicegame.constant.ErrorConstant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameRequest {
    @NotBlank(message = GAME_NAME_REQUIRED)
    @Size(min = 2, max = 50, message = GAME_NAME_LENGTH)
    private String gameName;
}
