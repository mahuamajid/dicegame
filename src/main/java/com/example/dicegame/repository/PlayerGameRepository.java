package com.example.dicegame.repository;

import com.example.dicegame.model.entity.PlayerGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerGameRepository extends JpaRepository<PlayerGame, Integer> {
    List<PlayerGame> findByGameId(Integer gameId);
}
