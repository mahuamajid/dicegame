package com.example.dicegame.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "player")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "gameList")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_name", length = 50, nullable = false)
    private String playerName;

    @Column(name = "age", nullable = false)
    private int age;

    @ManyToMany
    @JoinTable(
            name = "game_player", // join table name
            joinColumns = @JoinColumn(name = "player_id"), // foreign key in join table for Player
            inverseJoinColumns = @JoinColumn(name = "game_id") // foreign key for Game
    )
    private List<Game> gameList = new ArrayList<>();
}
