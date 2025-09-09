package com.example.dicegame.repository;

import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByGameName(String gameName);
    List<Game> findByWinnerPlayer(Player player);
}
