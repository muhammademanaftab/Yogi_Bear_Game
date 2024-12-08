package model;

/**
 * Represents Yogi in the game with a position on the board.
 */
public class Yogi {
    private Position position;

    /**
     * Constructs Yogi at the specified starting position.
     *
     * @param startPosition The initial position of Yogi.
     */
    public Yogi(Position startPosition) {
        this.position = startPosition;
    }

    /**
     * Gets the current position of Yogi.
     *
     * @return Yogi's current position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets a new position for Yogi.
     *
     * @param position The new position of Yogi.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Calculates Yogi's new position based on the given direction.
     *
     * @param direction The direction in which Yogi moves.
     * @return The new position after the move.
     */
    public Position move(Direction direction) {
        return position.translate(direction);
    }
}
