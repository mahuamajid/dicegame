package com.example.dicegame.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import static com.example.dicegame.constant.ErrorConstant.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerRequest {
    @NotBlank(message = PLAYER_NAME_REQUIRED)
    @Size(min = 2, max = 50, message = PLAYER_NAME_LENGTH)
    private String name;

    @NotNull(message = PLAYER_AGE_REQUIRED)
    @Min(value = 5, message = PLAYER_MIN_AGE)
    private Integer age;
}
