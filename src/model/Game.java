package model;

/**
 * Represents the game's state and logic, including levels, lives, and score.
 */
public class Game {

    private GameUtils currentLevel;
    private int lives;
    private int score;

    /**
     * Creates a new game with default settings.
     */
    public Game() {
        resetGame();
    }

    /**
     * Loads a given level into the game.
     *
     * @param level the level to load
     */
    public void loadGame(GameUtils level) {
        currentLevel = level;
    }

    /**
     * Moves Yogi in the given direction.
     *
     * @param d the direction to move
     * @return true if move is successful (and possibly completes level), false otherwise
     */
    public boolean moveYogi(Direction d) {
        if (currentLevel == null) return false;

        Position newPos = currentLevel.getYogi().move(d);
        if (!currentLevel.isValidPosition(newPos) || currentLevel.isObstacle(newPos)) return false;

        currentLevel.getYogi().setPosition(newPos);

        if (currentLevel.collectBasket(newPos)) {
            score++;
            if (currentLevel.allBasketsCollected()) return true;
        }

        return true;
    }

    /**
     * Decreases the player's lives by one.
     */
    public void loseLife() {
        lives--;
    }

    /**
     * Checks if the game is over.
     *
     * @return true if no lives remain, false otherwise
     */
    public boolean isGameOver() {
        return lives <= 0;
    }

    /**
     * Checks if the player has won the entire game.
     *
     * @return true if all levels are completed, false otherwise
     */
    public boolean isVictory() {
        return currentLevel != null
                && currentLevel.allBasketsCollected()
                && currentLevel.getLevelNumber() == Levels.getTotalLevels();
    }

    /**
     * Advances to the next level if the current one is completed.
     */
    public void completeLevel() {
        if (currentLevel != null && currentLevel.allBasketsCollected()) {
            int next = currentLevel.getLevelNumber() + 1;
            if (next <= Levels.getTotalLevels()) {
                currentLevel = new GameUtils(Levels.getLevel(next), next);
            }
        }
    }

    /**
     * Returns the current lives.
     *
     * @return current lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Returns the current score.
     *
     * @return current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns the current level.
     *
     * @return the current level, or null if none
     */
    public GameUtils getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Resets the game to initial state.
     */
    public void resetGame() {
        lives = 3;
        score = 0;
        currentLevel = null;
    }
}
