package model;

/**
 * Represents the directions in which movement is allowed.
 * Each direction is associated with x and y offsets. 
 */
public enum Direction {
    UP(0, -1), 
    DOWN(0, 1), 
    LEFT(-1, 0), 
    RIGHT(1, 0);

    public final int x;
    public final int y;

    /**
     * Constructing a Direction with the specified x and y offsets.
     *
     * @param x The horizontal offset for the direction.
     * @param y The vertical offset for the direction.
     */
    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
