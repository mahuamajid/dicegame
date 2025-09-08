package com.example.dicegame.repository.impl;

import com.example.dicegame.exception.PlayerException;
import com.example.dicegame.model.dto.request.PlayerSearchRequest;
import com.example.dicegame.model.dto.response.PlayerResponse;
import com.example.dicegame.model.dto.response.base.PaginatedResponse;
import com.example.dicegame.model.entity.QPlayer;
import com.example.dicegame.repository.PlayerCustomRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.dicegame.model.dto.response.base.ApiResponseFactory.buildPaginatedResponse;
import static com.example.dicegame.repository.predicate.PlayerPredicate.build;

@Repository
@RequiredArgsConstructor
public class PlayerCustomRepositoryImpl implements PlayerCustomRepository {
    private static final QPlayer qPlayer = QPlayer.player;
    private final JPAQueryFactory queryFactory;

    @Override
    public PaginatedResponse<PlayerResponse> pageData(PlayerSearchRequest searchRequest) throws PlayerException {
        Pageable pageable = PageRequest.of(searchRequest.getPage(), searchRequest.getSize());
        var predicate = build(searchRequest);
        int offset = searchRequest.getPage() * searchRequest.getSize();
        List<PlayerResponse> playerResponseList = queryFactory
                .select(Projections.fields(PlayerResponse.class,
                        qPlayer.id,
                        qPlayer.playerName,
                        qPlayer.age,
                        qPlayer.createTime
                ))
                .from(qPlayer)
                .where(predicate)
                .orderBy(qPlayer.id.asc())
                .offset(offset)
                .limit(searchRequest.getSize())
                .fetch();

        Long totalCount = queryFactory
                .select(qPlayer.count())
                .from(qPlayer)
                .fetchOne();
        Page<PlayerResponse> pageInfo = new PageImpl<>(playerResponseList, pageable, totalCount);
        return buildPaginatedResponse(searchRequest.getPage(), searchRequest.getSize(), pageInfo, pageInfo.getContent());
    }
}
