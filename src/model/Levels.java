package model;

import java.util.ArrayList;

public class Levels {
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
                levelRows.add("E   R   B ");
                levelRows.add("  T T T R ");
                levelRows.add("R   M     ");
                levelRows.add("B R       ");
                break;

            case 3:
                levelRows.add("E   T T   ");
                levelRows.add("R     B   ");
                levelRows.add("  M M     ");
                levelRows.add("  B   R R ");
                break;

            case 4:
                levelRows.add("E R   T T ");
                levelRows.add("  T   B   ");
                levelRows.add("B   M M R ");
                levelRows.add("    R     ");
                break;

            case 5:
                levelRows.add("E       B ");
                levelRows.add("R M T T T ");
                levelRows.add("B   M   R ");
                levelRows.add("    R   B ");
                break;

            default:
                throw new IllegalArgumentException("Level " + levelNumber + " is not defined.");
        }
        return levelRows;
    }

    public static int getTotalLevels() {
        return 5; // Adjusted to reflect the total number of levels.
    }
}
