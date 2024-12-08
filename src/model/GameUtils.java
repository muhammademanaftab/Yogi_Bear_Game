package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles game logic for each level, including Yogi and Ranger movements,
 * collecting baskets, and managing obstacles.
 */
public class GameUtils {

    private final GameID gameID;
    private final int rows, cols;
    private final LevelItem[][] level;
    private Yogi yogi;
    private final List<Ranger> rangers = new ArrayList<>();
    private int numBaskets;
    private int basketsCollected;
    private final Random random = new Random();

    /**
     * Constructs a new GameUtils instance based on the provided level layout.
     *
     * @param gameLevelRows The level layout as a list of strings.
     * @param gameID        The unique identifier for the level.
     */
    public GameUtils(ArrayList<String> gameLevelRows, GameID gameID) {
        this.gameID = gameID;
        rows = gameLevelRows.size();
        cols = gameLevelRows.stream().mapToInt(String::length).max().orElse(0);

        level = new LevelItem[rows][cols];
        numBaskets = 0;
        basketsCollected = 0;

        for (int i = 0; i < rows; i++) {
            String row = gameLevelRows.get(i);
            for (int j = 0; j < cols; j++) {
                char c = j < row.length() ? row.charAt(j) : ' ';
                switch (c) {
                    case 'Y' -> {
                        yogi = new Yogi(new Position(j, i));
                        level[i][j] = LevelItem.EMPTY;
                    }
                    case 'B' -> {
                        numBaskets++;
                        level[i][j] = LevelItem.BASKET;
                    }
                    case 'T' -> level[i][j] = LevelItem.TREE;
                    case 'M' -> level[i][j] = LevelItem.MOUNTAIN;
                    case 'E' -> {
                        level[i][j] = LevelItem.ENTRANCE;
                        if (yogi == null) {
                            yogi = new Yogi(new Position(j, i));
                        }
                    }
                    case 'R' -> {
                        rangers.add(new Ranger(new Position(j, i)));
                        level[i][j] = LevelItem.EMPTY;
                    }
                    default -> level[i][j] = LevelItem.EMPTY;
                }
            }
        }

        if (yogi == null) {
            throw new IllegalStateException("No starting position for Yogi ('Y' or 'E') found in the level!");
        }
    }

    /**
     * Moves all rangers randomly on the board.
     *
     * @return true if a ranger catches Yogi, false otherwise.
     */
    public boolean moveRangersRandomly() {
        Position yogiPosition = yogi.getPosition();
        Position entrance = getEntrance();
        boolean yogiCaught = false;

        for (Ranger ranger : rangers) {
            Position currentPosition = ranger.getPosition();
            List<Direction> possibleDirections = new ArrayList<>();

            for (Direction d : Direction.values()) {
                Position newPosition = currentPosition.translate(d);
                if (isValidPosition(newPosition) && isMovableForRanger(newPosition)) {
                    possibleDirections.add(d);
                }
            }

            if (!possibleDirections.isEmpty()) {
                Direction randomDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                Position newPosition = currentPosition.translate(randomDirection);
                ranger.setPosition(newPosition);

                if (newPosition.equals(yogiPosition) && !yogiPosition.equals(entrance)) {
                    yogiCaught = true;
                }
            }
        }
        return yogiCaught;
    }

    /**
     * Checks if the given position is valid and movable for a ranger.
     *
     * @param p The position to check.
     * @return true if the position is valid and movable, false otherwise.
     */
    private boolean isMovableForRanger(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        LevelItem li = level[p.y][p.x];

        if (li != LevelItem.EMPTY) {
            return false;
        }
        for (Ranger ranger : rangers) {
            if (ranger.getPosition().equals(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Handles Yogi's movement and checks if he encounters a ranger.
     *
     * @param newPosition The new position of Yogi.
     * @return true if Yogi encounters a ranger, false otherwise.
     */
    public boolean handleYogiMove(Position newPosition) {
        Position entrance = getEntrance();
        for (Ranger ranger : rangers) {
            if (ranger.getPosition().equals(newPosition) && !newPosition.equals(entrance)) {
                return true;
            }
        }
        return false;
    }

    public Yogi getYogi() {
        return yogi;
    }

    public void setYogi(Yogi yogi) {
        this.yogi = yogi;
    }

    public List<Ranger> getRangers() {
        return rangers;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public LevelItem[][] getLevel() {
        return level;
    }

    public GameID getGameID() {
        return gameID;
    }

    /**
     * Checks if the given position is valid within the board.
     *
     * @param p The position to check.
     * @return true if the position is valid, false otherwise.
     */
    public boolean isValidPosition(Position p) {
        return p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows;
    }

    /**
     * Checks if the given position contains an obstacle.
     *
     * @param p The position to check.
     * @return true if the position contains an obstacle, false otherwise.
     */
    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) {
            return true;
        }
        LevelItem li = level[p.y][p.x];
        return li == LevelItem.TREE || li == LevelItem.MOUNTAIN;
    }

    /**
     * Collects a basket at the given position, if present.
     *
     * @param p The position to check for a basket.
     * @return true if a basket was collected, false otherwise.
     */
    public boolean collectBasket(Position p) {
        if (isValidPosition(p) && level[p.y][p.x] == LevelItem.BASKET) {
            level[p.y][p.x] = LevelItem.EMPTY;
            basketsCollected++;
            return true;
        }
        return false;
    }

    /**
     * Checks if all baskets in the level have been collected.
     *
     * @return true if all baskets are collected, false otherwise.
     */
    public boolean allBasketsCollected() {
        return basketsCollected == numBaskets;
    }

    /**
     * Gets the entrance position on the board.
     *
     * @return The position of the entrance.
     * @throws IllegalStateException if no entrance is found.
     */
    public Position getEntrance() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (level[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("No entrance found in the level!");
    }
}
