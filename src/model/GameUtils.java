/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

public class GameUtils {

    public static ArrayList<String> getLevel(int levelNumber) {
        ArrayList<String> levelRows = new ArrayList<>();
        switch (levelNumber) {
            case 1:
                levelRows.add("E       B ");
                levelRows.add("  T T T   ");
                levelRows.add("    M     ");
                levelRows.add("B       R ");
                break;
            case 2:
                levelRows.add("E TTT ");
                levelRows.add("  M  ");
                levelRows.add("B   R");
                break;
            default:
                throw new IllegalArgumentException("Level " + levelNumber + " is not defined.");
        }
        return levelRows;
    }

    public static void printLevel(GameLevel level) {
        System.out.println("\nCurrent Level:");
        Position yogi = level.yogi;

        for (int y = 0; y < level.rows; y++) {
            for (int x = 0; x < level.cols; x++) {
                if (yogi.x == x && yogi.y == y) {
                    System.out.print("Y "); // Yogi's position
                } else {
                    System.out.print(level.level[y][x].representation + " ");
                }
            }
            System.out.println();
        }
    }
    
    public static int getTotalLevels() {
    return 2;
}

}
