//package view;
//
//import model.*;
//
//import java.util.Scanner;
//
//public class ConsoleMain {
//
//    public static void main(String[] args) {
//        Game game = new Game();
//        GameID gameID = new GameID("EASY", 1);
//        GameUtils gameUtils = new GameUtils(Levels.getLevel(1), gameID);
//        game.loadGame(gameUtils);
//
//        Scanner scanner = new Scanner(System.in);
//
//        printWelcomeMessage();
//        gameUtils.printLevel();
//
//        while (true) {
//            Direction direction = getPlayerMove(scanner);
//
//            // Attempt to move Yogi
//            if (!game.moveYogi(direction)) {
//                if (game.getLives() <= 0) {
//                    System.out.println("Game Over! You ran out of lives.");
//                    break;
//                }
//                System.out.println("Invalid move or obstacle!");
//            }
//
//            // Move rangers randomly after Yogi's move
//            gameUtils.moveRangersRandomly();
//
//            // Check if Yogi encounters a ranger
//            if (handleRangerEncounters(game, gameUtils)) {
//                gameUtils.printLevel();
//                continue; // Skip the rest of the turn if respawned
//            }
//
//            // Check if all baskets are collected
//            if (handleBasketCollection(game, gameUtils)) {
//                if (Levels.getTotalLevels() == gameUtils.getGameID().level) {
//                    System.out.println("You completed all levels! Final Score: " + game.getScore());
//                    break;
//                } else {
//                    // Load the next level
//                    int nextLevelNumber = gameUtils.getGameID().level + 1;
//                    gameUtils = new GameUtils(Levels.getLevel(nextLevelNumber),
//                            new GameID("EASY", nextLevelNumber));
//                    game.loadGame(gameUtils);
//                    System.out.println("Level " + nextLevelNumber + " loaded!");
//                    gameUtils.printLevel();
//                    continue;
//                }
//            }
//
//            gameUtils.printLevel(); // Print the level after each turn
//        }
//    }
//
//    private static void printWelcomeMessage() {
//        System.out.println("Welcome to the Yogi Bear game!");
//        System.out.println("Controls: W (up), A (left), S (down), D (right), Q (quit)");
//    }
//
//    private static Direction getPlayerMove(Scanner scanner) {
//        Direction direction = null;
//        while (direction == null) {
//            System.out.print("Enter your move: ");
//            String input = scanner.nextLine().trim().toUpperCase();
//
//            switch (input) {
//                case "W":
//                    direction = Direction.UP;
//                    break;
//                case "A":
//                    direction = Direction.LEFT;
//                    break;
//                case "S":
//                    direction = Direction.DOWN;
//                    break;
//                case "D":
//                    direction = Direction.RIGHT;
//                    break;
//                case "Q":
//                    System.out.println("Thank you for playing!");
//                    System.exit(0);
//                    break;
//                default:
//                    System.out.println("Invalid input. Use W, A, S, D, or Q.");
//            }
//        }
//        return direction;
//    }
//
//    private static boolean handleRangerEncounters(Game game, GameUtils gameUtils) {
//        Position yogiPosition = gameUtils.getYogi().getPosition();
//        Position entrance = gameUtils.getEntrance();
//
//        for (Ranger ranger : gameUtils.getRangers()) {
//            if (yogiPosition.equals(entrance)) {
//                // Yogi is at the entrance and safe
//                return false;
//            }
//            if (ranger.isNear(yogiPosition)) {
//                System.out.println("Yogi encountered a ranger! Losing a life...");
//                game.loseLife();
//                if (game.getLives() > 0) {
//                    // Respawn Yogi at the entrance
//                    gameUtils.getYogi().setPosition(entrance);
//                    System.out.println("Respawning Yogi at the entrance.");
//                    return true;
//                } else {
//                    System.out.println("Game Over! You lost all your lives.");
//                    System.exit(0);
//                }
//            }
//        }
//        return false;
//    }
//
//    private static boolean handleBasketCollection(Game game, GameUtils gameUtils) {
//        if (gameUtils.allBasketsCollected()) {
//            System.out.println("Congratulations! You collected all the baskets!");
//            return true;
//        }
//        return false;
//    }
//}
