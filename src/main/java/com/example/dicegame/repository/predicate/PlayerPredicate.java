package com.example.dicegame.repository.predicate;

import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.entity.QPlayer;
import com.querydsl.core.BooleanBuilder;

import java.util.Optional;

public class PlayerPredicate {
    private static final QPlayer qPlayer = QPlayer.player;

    public static BooleanBuilder build(PlayerSearchRequest searchRequest) {
        BooleanBuilder predicate = new BooleanBuilder();

        Optional.ofNullable(searchRequest.getId())
                .ifPresent(id -> predicate.and(qPlayer.id.eq(id)));

        return predicate;
    }
}
