package model;

import java.util.ArrayList;

/**
 * Provides level configurations for the Yogi Bear game.
 */
public class Levels {

    /**
     * Retrieves the layout of the specified level.
     *
     * @param levelNumber The number of the level to retrieve.
     * @return A list of strings representing the level's grid.
     */
    public static ArrayList<String> getLevel(int levelNumber) {
        ArrayList<String> levelRows = new ArrayList<>();
        switch (levelNumber) {
            case 1:
                levelRows.add("E M B R ");
                levelRows.add("   T    ");
                levelRows.add("    R   ");
                levelRows.add("  B     ");
                levelRows.add("        ");
                break;

            case 2:
                levelRows.add("E R    B ");
                levelRows.add("   T  T  ");
                levelRows.add(" R   B R ");
                levelRows.add("  B      ");
                levelRows.add("     T   ");
                break;

            case 3:
                levelRows.add("E R B     ");
                levelRows.add("   T   T  ");
                levelRows.add(" R  M     ");
                levelRows.add(" R  B  B  ");
                levelRows.add("   T      ");
                levelRows.add("      R   ");
                break;

            case 4:
                levelRows.add("E   B   T   ");
                levelRows.add("  T  B  T   ");
                levelRows.add("   R   M    ");
                levelRows.add("   R    M B ");
                levelRows.add("  R  T      ");
                levelRows.add("   M        ");
                break;

            case 5:
                levelRows.add("E  B    T   ");
                levelRows.add("  T   M     ");
                levelRows.add("  B   R     ");
                levelRows.add("   T      B ");
                levelRows.add("    R   T   ");
                levelRows.add(" B      M   ");
                levelRows.add("   T B   R  ");
                break;

            case 6:
                levelRows.add("E     T      ");
                levelRows.add("  B   R      ");
                levelRows.add("    T   M    ");
                levelRows.add("  R   B    T ");
                levelRows.add("   B    T    ");
                levelRows.add(" B   M       ");
                levelRows.add(" T M    R    ");
                break;

            case 7:
                levelRows.add("E  T    R     ");
                levelRows.add("    T  B      ");
                levelRows.add("       M      ");
                levelRows.add("  T   R   R   ");
                levelRows.add("B    T   B    ");
                levelRows.add("   R          ");
                levelRows.add("       B T    ");
                break;

            case 8:
                levelRows.add("E    T  B     ");
                levelRows.add("   R   B  T   ");
                levelRows.add(" R   M        ");
                levelRows.add("  T      R    ");
                levelRows.add("    T   M     ");
                levelRows.add(" T R          ");
                levelRows.add(" B     B  T   ");
                break;

            case 9:
                levelRows.add("E  T  R       ");
                levelRows.add("      M   T   ");
                levelRows.add("   R       B  ");
                levelRows.add("     T        ");
                levelRows.add("   B  R   M   ");
                levelRows.add(" T     R   B  ");
                levelRows.add(" R   B   T  B ");
                break;

            case 10:
                levelRows.add("E   T R       ");
                levelRows.add("R  M       T  ");
                levelRows.add(" B     R   B  ");
                levelRows.add(" R   M   T    ");
                levelRows.add("   R       B  ");
                levelRows.add("  M   T    B  ");
                levelRows.add("  B       R   ");
                break;

            default:
                throw new IllegalArgumentException("Level " + levelNumber + " is not defined.");
        }
        return levelRows;
    }

    /**
     * Returns the total number of levels.
     *
     * @return Total levels count.
     */
    public static int getTotalLevels() {
        return 10;
    }
}
