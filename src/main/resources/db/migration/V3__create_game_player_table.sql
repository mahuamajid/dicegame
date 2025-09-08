
-- ===========================================
-- Table: game_player
-- ===========================================

CREATE TABLE game_player (
    id                                              SERIAL PRIMARY KEY,
    player_id                                       INT NOT NULL,
    score                                           INT NOT NULL,
    state                                           VARCHAR(12) NOT NULL,
    game_id                                         INT NOT NULL,

    UNIQUE (player_id, game_id),
    CONSTRAINT fk_player FOREIGN KEY (player_id)    REFERENCES player(id),
    CONSTRAINT fk_game   FOREIGN KEY (game_id)      REFERENCES game(id)
);

-- Indexes for optimized queries
CREATE INDEX idx_game_player_state                  ON game_player(state);