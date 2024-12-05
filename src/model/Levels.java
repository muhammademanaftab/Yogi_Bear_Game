package model;

import java.util.ArrayList;

public class Levels {
    public static ArrayList<String> getLevel(int levelNumber) {
        ArrayList<String> levelRows = new ArrayList<>();
        switch (levelNumber) {
            case 1: // Small and simple: 6x6
                levelRows.add("E     ");
                levelRows.add("  B   ");
                levelRows.add("   T  ");
                levelRows.add("    R ");
                levelRows.add("  B   ");
                levelRows.add("      ");
                break;

            case 2: // Slightly harder: 7x7
                levelRows.add("E     B");
                levelRows.add("   T T ");
                levelRows.add("     R ");
                levelRows.add("  B    ");
                levelRows.add("    R  ");
                levelRows.add("   T   ");
                levelRows.add("     B ");
                break;

            case 3: // Medium: More obstacles and baskets, 8x8
                levelRows.add("E B    ");
                levelRows.add("  T  T ");
                levelRows.add("   M   ");
                levelRows.add("  R    ");
                levelRows.add("    B  ");
                levelRows.add("   T   ");
                levelRows.add("  B R  ");
                levelRows.add("       ");
                break;

            case 4: // Increasing difficulty: 9x9
                levelRows.add("E  B   ");
                levelRows.add("  T T  ");
                levelRows.add("    M  ");
                levelRows.add("  R    ");
                levelRows.add("   B   ");
                levelRows.add("    T  ");
                levelRows.add("  M R  ");
                levelRows.add("   B   ");
                levelRows.add("     R ");
                break;

            case 5: // Hard: 9x9 with more baskets and rangers
                levelRows.add("E  B   ");
                levelRows.add("  T T  ");
                levelRows.add(" B M R ");
                levelRows.add("    R  ");
                levelRows.add("  B    ");
                levelRows.add("    M  ");
                levelRows.add("   T   ");
                levelRows.add(" B  R  ");
                levelRows.add("  T    ");
                break;

            case 6: // Even harder: 10x10
                levelRows.add("E   T   ");
                levelRows.add("  B R B ");
                levelRows.add("   T M  ");
                levelRows.add("    R   ");
                levelRows.add("  B     ");
                levelRows.add("   M R  ");
                levelRows.add("  T B   ");
                levelRows.add(" B  R   ");
                levelRows.add("  M     ");
                levelRows.add("     T  ");
                break;

            case 7: // Challenging: 10x10
                levelRows.add("E T R   ");
                levelRows.add("  B T   ");
                levelRows.add("   M    ");
                levelRows.add("  R B   ");
                levelRows.add("    T   ");
                levelRows.add("   M B  ");
                levelRows.add(" T  R   ");
                levelRows.add("  M     ");
                levelRows.add("   T R  ");
                levelRows.add(" B      ");
                break;

            case 8: // Dense obstacles: 10x10
                levelRows.add("E R T B ");
                levelRows.add("  T M   ");
                levelRows.add("  R B   ");
                levelRows.add(" M      ");
                levelRows.add("   T    ");
                levelRows.add("  R T   ");
                levelRows.add("   B M  ");
                levelRows.add("  T R   ");
                levelRows.add(" B      ");
                levelRows.add("   T    ");
                break;

            case 9: // Maximum difficulty: 10x10
                levelRows.add("E T R T ");
                levelRows.add(" T M B  ");
                levelRows.add(" B M R  ");
                levelRows.add("   R T  ");
                levelRows.add(" B M    ");
                levelRows.add(" R T    ");
                levelRows.add("  M B   ");
                levelRows.add(" T  R   ");
                levelRows.add("   T M  ");
                levelRows.add(" B      ");
                break;

            case 10: // Insane level: 10x10 with maximum density
                levelRows.add("E T R T ");
                levelRows.add(" T M B R");
                levelRows.add(" B M R  ");
                levelRows.add(" R T B  ");
                levelRows.add(" B M T  ");
                levelRows.add(" T R B  ");
                levelRows.add(" M B T  ");
                levelRows.add(" T R M  ");
                levelRows.add(" R B M  ");
                levelRows.add(" M T    ");
                break;

            default:
                throw new IllegalArgumentException("Level " + levelNumber + " is not defined.");
        }
        return levelRows;
    }

    public static int getTotalLevels() {
        return 10; // Updated total levels.
    }
}
