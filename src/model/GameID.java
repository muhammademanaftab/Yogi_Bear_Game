package model;

import java.util.Objects;

/**
 * Represents a unique identifier for a game level, defined by its difficulty
 * and level number.
 */
public class GameID {
    public final String difficulty;
    public final int level;

    /**
     * Constructs a new GameID with the specified difficulty and level number.
     *
     * @param difficulty The difficulty level (e.g., "EASY").
     * @param level      The level number.
     */
    public GameID(String difficulty, int level) {
        this.difficulty = difficulty;
        this.level = level;
    }

    /**
     * Returns the hash code for this GameID, based on difficulty and level.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(difficulty, level);
    }

    /**
     * Compares this GameID with another object for equality.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GameID other = (GameID) obj;
        return level == other.level && Objects.equals(difficulty, other.difficulty);
    }
}
