package com.example.dicegame.model.entity;

import com.example.dicegame.constant.State;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
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

    @Column(name = "state", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.AVAILABLE;

    @Column(name = "create_time")
    private Instant createTime;

    @ManyToMany
    @JoinTable(
            name = "game_player", // join table name
            joinColumns = @JoinColumn(name = "player_id"), // foreign key in join table for Player
            inverseJoinColumns = @JoinColumn(name = "game_id") // foreign key for Game
    )
    private List<Game> gameList = new ArrayList<>();
}
