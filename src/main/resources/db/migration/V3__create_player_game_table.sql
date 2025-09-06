
-- ===========================================
-- Table: player_game
-- ===========================================

CREATE TABLE player_game (
    id                                      SERIAL PRIMARY KEY,
    player_id                               INT NOT NULL,
    game_id                                 INT NOT NULL,
    UNIQUE (player_id, game_id),
    CONSTRAINT fk_player FOREIGN KEY (player_id)   REFERENCES player(id),
    CONSTRAINT fk_game   FOREIGN KEY (game_id)     REFERENCES game(id)
);