CREATE DATABASE IF NOT EXISTS yogi_game;
USE yogi_game;

CREATE TABLE IF NOT EXISTS player_scores (
    id INT AUTO_INCREMENT PRIMARY KEY, -- Unique ID for each record
    player_name VARCHAR(255) NOT NULL, -- Name of the player
    score INT NOT NULL, -- Player's score
    game_duration BIGINT NOT NULL, -- Game duration in seconds
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Timestamp of when the record was created
);
