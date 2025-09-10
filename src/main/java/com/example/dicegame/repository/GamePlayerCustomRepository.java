package com.example.dicegame.repository;

public interface GamePlayerCustomRepository {
    int findByTotalWinnerPlayerScore(Integer winnerPlayerId, int prizeScore);
}
