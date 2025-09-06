package com.example.dicegame.model.entity;

import com.example.dicegame.constant.State;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "player")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "games")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "player_name", length = 50, nullable = false)
    private String playerName;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "score", nullable = false)
    @Builder.Default
    private int score = 0;

    @Column(name = "state", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.BEFORE_START;

    @ManyToMany
    @JoinTable(
            name = "player_game", // join table name
            joinColumns = @JoinColumn(name = "player_id"), // foreign key in join table for Player
            inverseJoinColumns = @JoinColumn(name = "game_id") // foreign key for Game
    )
    private Set<Game> games = new HashSet<>();
}
