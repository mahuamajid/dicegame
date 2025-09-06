package com.example.dicegame.repository;

import com.example.dicegame.model.entity.Game;
import com.example.dicegame.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
