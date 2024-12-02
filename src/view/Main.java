package view;

import model.*;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GameID gameID = new GameID("EASY", 1);
        GameLevel gameLevel = new GameLevel(GameUtils.getLevel(1), gameID);
        Game game = new Game();
        game.loadGame(gameLevel);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Yogi Bear game!");
        System.out.println("Controls: W (up), A (left), S (down), D (right), Q (quit)");
        GameUtils.printLevel(gameLevel);

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
                if (game.lives <= 0) {
                    System.out.println("Game Over! You ran out of lives.");
                    break;
                }
            }

            GameUtils.printLevel(gameLevel);

            if (gameLevel.allBasketsCollected()) {
                System.out.println("Congratulations! You collected all the baskets!");
                if (GameUtils.getTotalLevels() == gameLevel.gameID.level) {
                    System.out.println("You completed all levels! Final Score: " + game.getScore());
                    break;
                } else {
                    gameLevel = new GameLevel(GameUtils.getLevel(gameLevel.gameID.level + 1), new GameID("EASY", gameLevel.gameID.level + 1));
                    game.loadGame(gameLevel);
                    GameUtils.printLevel(gameLevel);
                }
            }
        }
    }
}
