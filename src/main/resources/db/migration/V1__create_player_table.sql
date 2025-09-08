
-- ===========================================
-- Table: player
-- ===========================================

CREATE TABLE player (
        id                              SERIAL PRIMARY KEY,
        player_name                     VARCHAR(50) NOT NULL,
        age                             INT NOT NULL
);
