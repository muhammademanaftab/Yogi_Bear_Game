package view;

import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameID gameID = new GameID("EASY", 1);
        GameUtils gameUtils = new GameUtils(Levels.getLevel(1), gameID);
        Game game = new Game();
        game.loadGame(gameUtils);

        Scanner scanner = new Scanner(System.in);

        printWelcomeMessage();
        gameUtils.printLevel();

        while (true) {
            Direction direction = getPlayerMove(scanner);

            if (!game.moveYogi(direction)) {
                if (game.getLives() <= 0) {
                    System.out.println("Game Over! You ran out of lives.");
                    break;
                }
                System.out.println("Invalid move or obstacle!");
            }

            // Move rangers randomly after Yogi's move
            gameUtils.moveRangersRandomly();

            // Handle Yogi's interaction with rangers
            if (handleRangerEncounters(game, gameUtils)) {
                gameUtils.printLevel(); // Always print the map after respawning
                continue; // Skip further processing for this turn
            }

            // Check if all baskets have been collected
            if (handleBasketCollection(game, gameUtils)) {
                if (Levels.getTotalLevels() == gameUtils.getGameID().level) {
                    System.out.println("You completed all levels! Final Score: " + game.getScore());
                    break;
                } else {
                    // Load the next level
                    gameUtils = new GameUtils(Levels.getLevel(gameUtils.getGameID().level + 1),
                            new GameID("EASY", gameUtils.getGameID().level + 1));
                    game.loadGame(gameUtils);
                    gameUtils.printLevel();
                    continue; // Skip further processing for this turn
                }
            }

            gameUtils.printLevel(); // Ensure the map is printed after a move
        }
    }

    private static void printWelcomeMessage() {
        System.out.println("Welcome to the Yogi Bear game!");
        System.out.println("Controls: W (up), A (left), S (down), D (right), Q (quit)");
    }

    private static Direction getPlayerMove(Scanner scanner) {
        Direction direction = null;
        while (direction == null) {
            System.out.print("Enter your move: ");
            String input = scanner.nextLine().trim().toUpperCase();

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
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Use W, A, S, D, or Q.");
            }
        }
        return direction;
    }

    private static boolean handleRangerEncounters(Game game, GameUtils gameUtils) {
        Position yogiPosition = gameUtils.getYogi().getPosition();
        for (Ranger ranger : gameUtils.getRangers()) {
            if (ranger.isNear(yogiPosition)) {
                System.out.println("Yogi encountered a ranger! Losing a life...");
                game.loseLife();
                if (game.getLives() > 0) {
                    Position entrance = findEntrance(gameUtils);
                    gameUtils.getYogi().setPosition(entrance);
                    System.out.println("Respawning Yogi at the entrance.");
                    return true; // Respawn happened, skip further processing this turn
                } else {
                    System.out.println("Game Over! You lost all your lives.");
                    System.exit(0);
                }
            }
        }
        return false; // No ranger encounter
    }

    private static boolean handleBasketCollection(Game game, GameUtils gameUtils) {
        if (gameUtils.allBasketsCollected()) {
            System.out.println("Congratulations! You collected all the baskets!");
            return true; // Baskets collected, level complete
        }
        return false; // Baskets not yet collected
    }

    private static Position findEntrance(GameUtils gameUtils) {
        for (int y = 0; y < gameUtils.getRows(); y++) {
            for (int x = 0; x < gameUtils.getCols(); x++) {
                if (gameUtils.getLevel()[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("No entrance found in the level!");
    }
}
