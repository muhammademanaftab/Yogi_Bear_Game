package model;

import java.util.Objects;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position translate(Direction d) {
        return new Position(x + d.x, y + d.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Check if both references are the same
        if (obj == null || getClass() != obj.getClass()) return false; // Null or different class
        Position other = (Position) obj;
        return x == other.x && y == other.y; // Compare x and y values
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y); // Generate hash based on x and y
    }

    @Override
    public String toString() {
        return "Position{x=" + x + ", y=" + y + "}"; // For debugging purposes
    }
}
