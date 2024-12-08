package model;

/**
 * Represents a ranger in the game with a specific position on the board.
 */
public class Ranger {
    private Position position;

    /**
     * Constructs a ranger at the specified position.
     *
     * @param position The initial position of the ranger.
     */
    public Ranger(Position position) {
        this.position = position;
    }

    /**
     * Gets the current position of the ranger.
     *
     * @return The ranger's position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets a new position for the ranger.
     *
     * @param position The new position of the ranger.
     */
    public void setPosition(Position position) {
        this.position = position;
    }
}
