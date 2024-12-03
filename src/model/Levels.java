package model;

import java.util.ArrayList;

public class Levels {
    public static ArrayList<String> getLevel(int levelNumber) {
        ArrayList<String> levelRows = new ArrayList<>();
        switch (levelNumber) {
            case 1: // Easy level: 5x5
                levelRows.add("E    ");
                levelRows.add("  T  ");
                levelRows.add("     ");
                levelRows.add("  B  ");
                levelRows.add("    R");
                break;

            case 2: // Slightly harder: 7x7
                levelRows.add("E     B");
                levelRows.add("   T T ");
                levelRows.add("       ");
                levelRows.add("   R   ");
                levelRows.add("     T ");
                levelRows.add("       ");
                levelRows.add("    R  ");
                break;

            case 3: // Medium difficulty: 9x9
                levelRows.add("E        ");
                levelRows.add("   T     ");
                levelRows.add("     M   ");
                levelRows.add("    B    ");
                levelRows.add(" R       ");
                levelRows.add("       R ");
                levelRows.add("   M     ");
                levelRows.add("     T   ");
                levelRows.add("        B");
                break;

            case 4: // Harder: 11x11
                levelRows.add("E          ");
                levelRows.add("  T T       ");
                levelRows.add("      M     ");
                levelRows.add(" R          ");
                levelRows.add("     B      ");
                levelRows.add("         R  ");
                levelRows.add("     M      ");
                levelRows.add("        T   ");
                levelRows.add("    R       ");
                levelRows.add("       B    ");
                levelRows.add("  R         ");
                break;

            case 5: // Hardest: 13x13
                levelRows.add("E           ");
                levelRows.add("  T   T      ");
                levelRows.add("    M        ");
                levelRows.add("  B      R   ");
                levelRows.add("             ");
                levelRows.add("      R   T  ");
                levelRows.add("             ");
                levelRows.add("    M        ");
                levelRows.add("  T          ");
                levelRows.add("       B     ");
                levelRows.add("             ");
                levelRows.add("    R        ");
                levelRows.add("         B   ");
                break;

            default:
                throw new IllegalArgumentException("Level " + levelNumber + " is not defined.");
        }
        return levelRows;
    }

    public static int getTotalLevels() {
        return 5; // Reflects the total number of levels.
    }
}
