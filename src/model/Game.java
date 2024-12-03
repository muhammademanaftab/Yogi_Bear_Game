package model;

public class Game {

    private GameUtils currentLevel;
    private int lives;
    private int score;

    public Game() {
        lives = 3;
        score = 0;
    }

    public void loadGame(GameUtils level) {
        currentLevel = level;
    }

    public boolean moveYogi(Direction d) {
        if (currentLevel == null) {
            return false;
        }

        Position newPos = currentLevel.getYogi().move(d);

        if (!currentLevel.isValidPosition(newPos)) {
            return false;
        }

        if (currentLevel.isObstacle(newPos)) {
            return false;
        }

        currentLevel.getYogi().setPosition(newPos);

//        if (!newPos.equals(currentLevel.getEntrance()) && currentLevel.isNearRanger(newPos)) {
//            loseLife();
//            if (lives > 0) {
//                currentLevel.getYogi().setPosition(currentLevel.getEntrance());
//            } else {
//                return false;
//            }
//        }

        if (currentLevel.collectBasket(newPos)) {
            score++;
            if (currentLevel.allBasketsCollected()) {
                return true;
            }
        }

        return true;
    }

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

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            System.out.println("Game Over!");
        } else {
            System.out.println("Respawning Yogi at the entrance.");
        }
    }

    public boolean isGameOver() {
        return lives <= 0;
    }

    public boolean isVictory() {
        return currentLevel != null && currentLevel.allBasketsCollected() && currentLevel.getGameID().level == Levels.getTotalLevels();
    }

    public void completeLevel() {
        if (currentLevel != null && currentLevel.allBasketsCollected()) {
            int nextLevel = currentLevel.getGameID().level + 1;
            if (nextLevel > Levels.getTotalLevels()) {
                return; // No more levels, victory condition
            }
            currentLevel = new GameUtils(Levels.getLevel(nextLevel), new GameID("EASY", nextLevel));
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
}
