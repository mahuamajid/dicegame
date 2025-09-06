package com.example.dicegame.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "game")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "playerList")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "game_name", length = 50, nullable = false)
    private String gameName;

    @Column(name = "started")
    @Builder.Default
    private boolean started = false;

    @Column(name = "finished")
    @Builder.Default
    private boolean finished = false;

    @Column(name = "target_score")
    private Integer targetScore;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "winner_player_id", referencedColumnName = "id")
    private Player winnerPlayer; // null until finished

    @ManyToMany(mappedBy = "gameList")  // Bidirectional mapping
    private List<Player> playerList = new ArrayList<>();
}
