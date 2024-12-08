package model;

import java.util.Objects;

/**
 * Represents a position on the game board with x and y coordinates.
 */
public class Position {

    private final int x;
    private final int y;
    
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }


    /**
     * @return the y
     */
    
    public int getY() {
        return y;
    }

    /**
     * Constructs a position with the specified coordinates.
     *
     * @param x The x-coordinate of the position.
     * @param y The y-coordinate of the position.
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Translates the position by a given direction.
     *
     * @param d The direction to translate the position.
     * @return A new position translated in the given direction.
     */
    public Position translate(Direction d) {
        return new Position(getX() + d.x, getY() + d.y);
    }

    /**
     * Checks if this position is equal to another object.
     *
     * @param obj The object to compare with.
     * @return True if the object is a position with the same coordinates.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position other = (Position) obj;
        return getX() == other.getX() && getY() == other.getY();
    }

    /**
     * Generates a hash code for the position.
     *
     * @return The hash code based on the coordinates.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    /**
     * Returns a string representation of the position.
     *
     * @return A string in the format Position{x=xValue, y=yValue}.
     */
    @Override
    public String toString() {
        return "Position{x=" + getX() + ", y=" + getY() + "}";
    }
}
