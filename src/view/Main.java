package view;

import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameID gameID = new GameID("EASY", 1);
        GameUtils gameUtils = new GameUtils(GameUtils.getLevel(1), gameID);
        Game game = new Game();
        game.loadGame(gameUtils);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Yogi Bear game!");
        System.out.println("Controls: W (up), A (left), S (down), D (right), Q (quit)");
        gameUtils.printLevel();

        while (true) {

            System.out.print("Enter your move: ");
            String input = scanner.nextLine().trim().toUpperCase();

            Direction direction = null;
            switch (input) {
                case "W":
                    direction = Direction.UP;
                    break;
                case "A":
                    direction = Direction.LEFT;
                    break;
                case "S":
                    direction = Direction.DOWN;
                    break;
                case "D":
                    direction = Direction.RIGHT;
                    break;
                case "Q":
                    System.out.println("Game Over!");
                    return;
                default:
                    System.out.println("Invalid input. Use W, A, S, D, or Q.");
                    continue;
            }

            if (!game.moveYogi(direction)) {
                System.out.println("Invalid move or obstacle!");
                if (game.getLives() <= 0) {
                    System.out.println("Game Over! You ran out of lives.");
                    break;
                }
            }

// Move ranger randomly after Yogi's move
            gameUtils.moveRangerRandomly();
            gameUtils.printLevel();

            // Check if all baskets have been collected
            if (gameUtils.allBasketsCollected()) {
                System.out.println("Congratulations! You collected all the baskets!");
                if (GameUtils.getTotalLevels() == gameUtils.getGameID().level) {
                    System.out.println("You completed all levels! Final Score: " + game.getScore());
                    break;
                } else {
                    // Load the next level
                    gameUtils = new GameUtils(GameUtils.getLevel(gameUtils.getGameID().level + 1),
                            new GameID("EASY", gameUtils.getGameID().level + 1));
                    game.loadGame(gameUtils);
                    gameUtils.printLevel();
                }
            }

            // Check if Yogi is near the ranger and trigger life loss if needed
            Position yogiPosition = gameUtils.getYogi().getPosition();
            if (gameUtils.getRanger().isNear(yogiPosition)) {
                System.out.println("Yogi encountered a ranger! Losing a life...");
                game.loseLife();
                if (game.getLives() > 0) {
                    // Respawn Yogi at the entrance
                    Position entrance = new Position(0, 0); // Default entrance position, can be adjusted
                    for (int y = 0; y < gameUtils.getRows(); y++) {
                        for (int x = 0; x < gameUtils.getCols(); x++) {
                            if (gameUtils.getLevel()[y][x] == LevelItem.ENTRANCE) {
                                entrance = new Position(x, y);
                                break;
                            }
                        }
                    }
                    gameUtils.getYogi().setPosition(entrance);
                    System.out.println("Respawning Yogi at the entrance.");
                    
            gameUtils.printLevel();
                } else {
                    System.out.println("Game Over! You lost all your lives.");
                    break;
                }
            }
        }
    }
}
