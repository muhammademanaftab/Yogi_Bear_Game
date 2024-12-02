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

            gameUtils.printLevel();

            if (gameUtils.allBasketsCollected()) {
                System.out.println("Congratulations! You collected all the baskets!");
                if (GameUtils.getTotalLevels() == gameUtils.getGameID().level) {
                    System.out.println("You completed all levels! Final Score: " + game.getScore());
                    break;
                } else {
                    gameUtils = new GameUtils(GameUtils.getLevel(gameUtils.getGameID().level + 1),
                            new GameID("EASY", gameUtils.getGameID().level + 1));
                    game.loadGame(gameUtils);
                    gameUtils.printLevel();
                }
            }
        }
    }
}
