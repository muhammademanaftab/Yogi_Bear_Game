/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Game {

    private GameLevel currentLevel;
    public int lives;
    private int score;

    public Game() {
        lives = 3;
        score = 0;
    }

    public void loadGame(GameLevel level) {
        currentLevel = level;
    }

    public boolean moveYogi(Direction d) {
        if (currentLevel == null) {
            return false;
        }

        Position newPos = currentLevel.yogi.translate(d);

        if (!currentLevel.isValidPosition(newPos)) {
            return false;
        }

        if (currentLevel.isObstacle(newPos)) {
            return false;
        }

        currentLevel.yogi = newPos;

        if (currentLevel.isNearRanger(newPos)) {
            loseLife();
            if (lives > 0) {
                currentLevel.yogi = findEntrance();
            } else {
                return false;
            }
        }

        if (currentLevel.collectBasket(newPos)) {
            score++;

            if (currentLevel.allBasketsCollected()) {
                
                return true;
            }
        }

        return true;
    }
    
    public boolean isLastLevel() {
    return currentLevel.gameID.level == GameUtils.getTotalLevels();
}


    private Position findEntrance() {
        for (int y = 0; y < currentLevel.rows; y++) {
            for (int x = 0; x < currentLevel.cols; x++) {
                if (currentLevel.level[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }

        // If no entrance is found, end the game gracefully
        System.out.println("No entrance found in the level! Ending game.");
        System.exit(1); // Exit the game with an error code
        return null; // Will never be reached, added for syntax completeness
    }

    public void loseLife() {
        lives--;
        if (lives > 0) {
            System.out.println("Respawning Yogi at the entrance.");
        } else {
            System.out.println("Game Over!");
        }
    }

    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }
}
