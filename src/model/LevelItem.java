package model;

/**
 * Represents the various items that can appear on the game board.
 */
public enum LevelItem {
    EMPTY(' '),
    TREE('T'),
    MOUNTAIN('M'),
    BASKET('B'),
    RANGER('R'),
    ENTRANCE('E'),
    YOGI('Y');

    private final char representation;

    /**
     * Constructs a LevelItem with a specific character representation.
     *
     * @param representation The character symbol representing the item.
     */
    LevelItem(char representation) {
        this.representation = representation;
    }
}
