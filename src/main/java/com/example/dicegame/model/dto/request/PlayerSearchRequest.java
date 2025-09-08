package com.example.dicegame.model.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerSearchRequest {
    private Integer id;
    @Builder.Default
    private Integer page = 0;
    @Builder.Default
    private Integer size = 10;
}
