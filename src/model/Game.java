package model;

public class Game {

    private GameUtils currentLevel;
    private int lives;
    private int score;

    public Game() {
        resetGame();
    }

    public void loadGame(GameUtils level) {
        currentLevel = level;
    }

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

    public void loseLife() {
        lives--;
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isVictory() {
        return currentLevel != null 
                && currentLevel.allBasketsCollected() 
                && currentLevel.getLevelNumber() == Levels.getTotalLevels();
    }

    public void completeLevel() {
        if (currentLevel != null && currentLevel.allBasketsCollected()) {
            int nextLevel = currentLevel.getLevelNumber() + 1;
            if (nextLevel > Levels.getTotalLevels()) {
                return;
            }
            currentLevel = new GameUtils(Levels.getLevel(nextLevel), nextLevel);
        }
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public GameUtils getCurrentLevel() {
        return currentLevel;
    }

    public void resetGame() {
        lives = 3;
        score = 0;
        currentLevel = null;
    }
}
