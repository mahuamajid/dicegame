package com.example.dicegame.repository;

import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Integer> {
    List<GamePlayer> findByGameId(Integer gameId);
    List<GamePlayer> findByGameIn(List<Game> gameList);
}
