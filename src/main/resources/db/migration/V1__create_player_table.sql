
-- ===========================================
-- Table: player
-- ===========================================

CREATE TABLE player (
        id                              SERIAL PRIMARY KEY,

        player_name                     VARCHAR(50) NOT NULL,
        age                             INT NOT NULL,
        score                           INT NOT NULL,
        state                           VARCHAR(12) NOT NULL
);

-- Indexes for optimized queries
CREATE INDEX idx_player_state           ON player(state);
