
-- ===========================================
-- Table: game
-- ===========================================

CREATE TABLE game (
        id                                      SERIAL PRIMARY KEY,

        started                                 BOOLEAN DEFAULT FALSE,
        finished                                BOOLEAN DEFAULT FALSE,
        target_score                            INT,
        winner_player_id                        INT,
        CONSTRAINT fk_winner_player     FOREIGN KEY (winner_player_id)          REFERENCES player(id)
);

-- Indexes for optimized queries
--CREATE INDEX idx_game_winner_player_id          ON game(winner_player_id);
