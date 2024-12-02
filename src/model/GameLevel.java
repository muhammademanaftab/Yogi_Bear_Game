/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

public class GameLevel {

    public final GameID gameID;
    public final int rows, cols;
    public final LevelItem[][] level;
    public Position yogi;
    private int numBaskets;
    private int basketsCollected;

    public GameLevel(ArrayList<String> gameLevelRows, GameID gameID) {
        this.gameID = gameID;
        rows = gameLevelRows.size();
        cols = gameLevelRows.stream().mapToInt(String::length).max().orElse(0);

        System.out.println("Rows: " + rows + ", Cols: " + cols);
        level = new LevelItem[rows][cols];
        numBaskets = 0;
        basketsCollected = 0;
        yogi = null; // Ensure Yogi is initialized

        for (int i = 0; i < rows; i++) {
            String row = gameLevelRows.get(i);
            for (int j = 0; j < cols; j++) {
                char c = j < row.length() ? row.charAt(j) : ' ';
                switch (c) {
                    case 'Y': // Explicit Yogi position
                        yogi = new Position(j, i);
                        level[i][j] = LevelItem.EMPTY; // Set to EMPTY as Yogi is dynamic
                        break;
                    case 'B':
                        numBaskets++;
                        level[i][j] = LevelItem.BASKET;
                        break;
                    case 'T':
                        level[i][j] = LevelItem.TREE;
                        break;
                    case 'M':
                        level[i][j] = LevelItem.MOUNTAIN;
                        break;
                    case 'E': // Entrance
                        level[i][j] = LevelItem.ENTRANCE;
                        if (yogi == null) { // If Yogi is not explicitly placed, use the entrance
                            yogi = new Position(j, i);
                        }
                        break;
                    case 'R':
                        level[i][j] = LevelItem.RANGER;
                        break;
                    default:
                        level[i][j] = LevelItem.EMPTY;
                        break;
                }
            }
        }

        if (yogi == null) {
            throw new IllegalStateException("No starting position for Yogi ('Y' or 'E') found in the level!");
        }

        // Print the level for debugging
//    System.out.println("Level array contents:");
//    for (int y = 0; y < rows; y++) {
//        for (int x = 0; x < cols; x++) {
//            if (yogi.x == x && yogi.y == y) {
//                System.out.print("Y "); // Dynamically show Yogi's position
//            } else {
//                System.out.print(level[y][x].representation + " ");
//            }
//        }
//        System.out.println();
//    }
    }

    public boolean isValidPosition(Position p) {
        boolean isValid = p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows;

//    if (!isValid) {
//        System.out.println("isValidPosition: Position (" + p.x + ", " + p.y + ") is invalid. "
//            + "Bounds: cols=" + cols + ", rows=" + rows);
//    } else {
//        System.out.println("isValidPosition: Position (" + p.x + ", " + p.y + ") is valid.");
//    }
        return isValid;
    }

    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) {
            return true;
        }
        LevelItem li = level[p.y][p.x];
        boolean isObstacle = li == LevelItem.TREE || li == LevelItem.MOUNTAIN;
//    if (isObstacle) {
//        System.out.println("isObstacle: Position (" + p.x + ", " + p.y + ") is an obstacle (" + li + ").");
//    }
        return isObstacle;
    }

    public boolean collectBasket(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        if (level[p.y][p.x] == LevelItem.BASKET) {
            level[p.y][p.x] = LevelItem.EMPTY;
            basketsCollected++;
            return true;
        }
        return false;
    }

    public boolean allBasketsCollected() {
        return basketsCollected == numBaskets;
    }

    public boolean isNearRanger(Position p) {
        for (Direction d : Direction.values()) {
            Position neighbor = p.translate(d);
            if (isValidPosition(neighbor) && level[neighbor.y][neighbor.x] == LevelItem.RANGER) {
                return true;
            }
        }
        return false;
    }

}
