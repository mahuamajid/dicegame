package com.example.dicegame.repository.impl;

import com.example.dicegame.model.entity.GamePlayer;
import com.example.dicegame.model.entity.QGame;
import com.example.dicegame.model.entity.QGamePlayer;
import com.example.dicegame.repository.GamePlayerCustomRepository;
import com.example.dicegame.repository.GamePlayerRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GamePlayerCustomRepositoryImpl implements GamePlayerCustomRepository {
    private static final QGame qGame = QGame.game;
    private static final QGamePlayer qGamePlayer = QGamePlayer.gamePlayer;
    private final JPAQueryFactory queryFactory;
    private final GamePlayerRepository gamePlayerRepository;

    @Override
    public int findByTotalWinnerPlayerScore(Integer winnerPlayerId, int prizeScore) {
        List<GamePlayer> gamePlayerList = queryFactory
                .selectFrom(qGamePlayer)
                .join(qGamePlayer.game, qGame)
                .where(qGamePlayer.player.id.eq(winnerPlayerId)
                        .and(qGame.winnerPlayer.id.eq(winnerPlayerId)))
                .fetch();


        int score = gamePlayerList.stream().mapToInt(GamePlayer::getScore).sum();
        if (score >= prizeScore) {
            gamePlayerList.forEach(gamePlayer -> gamePlayer.setScore(0));
            gamePlayerRepository.saveAll(gamePlayerList);
        }
        return score;
    }
}
