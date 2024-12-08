package model;

/**
 * Represents the game logic, including player movement, level progression,
 * and score tracking.
 */
public class Game {

    private GameUtils currentLevel;
    private int lives;
    private int score;

    /**
     * Initializes the game with default values for lives and score.
     */
    public Game() {
        resetGame();
    }

    /**
     * Loads the specified level into the game.
     *
     * @param level The level to load.
     */
    public void loadGame(GameUtils level) {
        currentLevel = level;
    }

    /**
     * Moves Yogi in the specified direction, handles obstacles, and collects baskets.
     *
     * @param d The direction in which Yogi moves.
     * @return true if the move is successful, false otherwise.
     */
    public boolean moveYogi(Direction d) {
        if (currentLevel == null) {
            return false;
        }

        Position newPos = currentLevel.getYogi().move(d);

        if (!currentLevel.isValidPosition(newPos) || currentLevel.isObstacle(newPos)) {
            return false;
        }

        currentLevel.getYogi().setPosition(newPos);

        if (currentLevel.collectBasket(newPos)) {
            score++;
            if (currentLevel.allBasketsCollected()) {
                return true;
            }
        }

        return true;
    }

    /**
     * Finds the entrance position in the current level.
     *
     * @return The position of the entrance.
     */
    private Position findEntrance() {
        for (int y = 0; y < currentLevel.getRows(); y++) {
            for (int x = 0; x < currentLevel.getCols(); x++) {
                if (currentLevel.getLevel()[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("No entrance found in the level!");
    }

    /**
     * Decreases the player's lives. If no lives remain, the game ends.
     */
    public void loseLife() {
        lives--;
        if (lives <= 0) {
            System.out.println("Game Over!");
        } else {
            System.out.println("Respawning Yogi at the entrance.");
        }
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the player has no lives remaining, false otherwise.
     */
    public boolean isGameOver() {
        return lives <= 0;
    }

    /**
     * Checks if the player has completed all levels and won the game.
     *
     * @return true if all levels are completed, false otherwise.
     */
    public boolean isVictory() {
        return currentLevel != null 
                && currentLevel.allBasketsCollected() 
                && currentLevel.getGameID().level == Levels.getTotalLevels();
    }

    /**
     * Completes the current level and loads the next level if available.
     */
    public void completeLevel() {
        if (currentLevel != null && currentLevel.allBasketsCollected()) {
            int nextLevel = currentLevel.getGameID().level + 1;
            if (nextLevel > Levels.getTotalLevels()) {
                return; // No more levels, victory condition
            }
            currentLevel = new GameUtils(Levels.getLevel(nextLevel), new GameID("EASY", nextLevel));
        }
    }

    /**
     * Returns the remaining lives of the player.
     *
     * @return The number of lives left.
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns the player's current score.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current level being played.
     *
     * @return The current level.
     */
    public GameUtils getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Resets the game to its initial state, including lives, score, and level.
     */
    public void resetGame() {
        lives = 3;
        score = 0;
        currentLevel = null;
    }
}
