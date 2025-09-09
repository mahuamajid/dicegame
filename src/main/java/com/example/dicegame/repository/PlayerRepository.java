package com.example.dicegame.repository;

import com.example.dicegame.model.enums.State;
import com.example.dicegame.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Integer> {
    List<Player> findByIdInAndState(List<Integer> playerIdList, State state);
}
