package com.example.dicegame.model.entity;

import com.example.dicegame.constant.State;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_player")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "score", nullable = false)
    @Builder.Default
    private int score = 0;

    @Column(name = "state", nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private State state = State.BEFORE_START;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
}
