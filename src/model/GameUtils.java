package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles a single game's level data and logic, including loading the level,
 * tracking Yogi, rangers, baskets, and obstacles.
 */
public class GameUtils {

    private final int level;
    private final int rows, cols;
    private final LevelItem[][] levelItems;
    private Yogi yogi;
    private final List<Ranger> rangers = new ArrayList<>();
    private int numBaskets;
    private int basketsCollected;
    private final Random random = new Random();

    /**
     * Constructs a level from the given rows and level number.
     *
     * @param gameLevelRows rows representing the level
     * @param level the level number
     */
    public GameUtils(ArrayList<String> gameLevelRows, int level) {
        this.level = level;
        rows = gameLevelRows.size();
        cols = gameLevelRows.stream().mapToInt(String::length).max().orElse(0);
        levelItems = new LevelItem[rows][cols];

        for (int i = 0; i < rows; i++) {
            String row = gameLevelRows.get(i);
            for (int j = 0; j < cols; j++) {
                char c = j < row.length() ? row.charAt(j) : ' ';
                switch (c) {
                    case 'Y' -> {
                        yogi = new Yogi(new Position(j, i));
                        levelItems[i][j] = LevelItem.EMPTY;
                    }
                    case 'B' -> {
                        numBaskets++;
                        levelItems[i][j] = LevelItem.BASKET;
                    }
                    case 'T' -> levelItems[i][j] = LevelItem.TREE;
                    case 'M' -> levelItems[i][j] = LevelItem.MOUNTAIN;
                    case 'E' -> {
                        levelItems[i][j] = LevelItem.ENTRANCE;
                        if (yogi == null) yogi = new Yogi(new Position(j, i));
                    }
                    case 'R' -> {
                        rangers.add(new Ranger(new Position(j, i)));
                        levelItems[i][j] = LevelItem.EMPTY;
                    }
                    default -> levelItems[i][j] = LevelItem.EMPTY;
                }
            }
        }

        if (yogi == null) {
            throw new IllegalStateException("No starting position found for Yogi.....!");
        }
    }

    /**
     * Moves all rangers randomly, possibly catching Yogi if not on the entrance.
     *
     * @return true if a ranger catches Yogi, false otherwise
     */
    public boolean moveRangersRandomly() {
        Position yogiPosition = yogi.getPosition();
        Position entrance = getEntrance();
        boolean yogiCaught = false;

        for (Ranger ranger : rangers) {
            Position current = ranger.getPosition();
            List<Direction> options = new ArrayList<>();

            for (Direction d : Direction.values()) {
                Position next = current.translate(d);
                if (isValidPosition(next) && isMovableForRanger(next)) {
                    options.add(d);
                }
            }

            if (!options.isEmpty()) {
                Direction chosen = options.get(random.nextInt(options.size()));
                Position newPos = current.translate(chosen);
                ranger.setPosition(newPos);

                if (newPos.equals(yogiPosition) && !yogiPosition.equals(entrance)) {
                    yogiCaught = true;
                }
            }
        }
        return yogiCaught;
    }

    private boolean isMovableForRanger(Position p) {
        if (!isValidPosition(p) || levelItems[p.getY()][p.getX()] != LevelItem.EMPTY) return false;
        for (Ranger r : rangers) {
            if (r.getPosition().equals(p)) return false;
        }
        return true;
    }

    /**
     * @return Yogi's character object
     */
    public Yogi getYogi() {
        return yogi;
    }

    /**
     * @return list of all rangers in this level
     */
    public List<Ranger> getRangers() {
        return rangers;
    }

    /**
     * @return number of rows in this level
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return number of columns in this level
     */
    public int getCols() {
        return cols;
    }

    /**
     * @return 2D array of all items in this level
     */
    public LevelItem[][] getLevel() {
        return levelItems;
    }

    /**
     * @return the level number
     */
    public int getLevelNumber() {
        return level;
    }

    /**
     * Checks if the position is within the level boundaries.
     *
     * @param p the position to check
     * @return true if valid, false otherwise
     */
    public boolean isValidPosition(Position p) {
        return p.getX() >= 0 && p.getY() >= 0 && p.getX() < cols && p.getY() < rows;
    }

    /**
     * Checks if the position is blocked by an obstacle.
     *
     * @param p the position to check
     * @return true if obstacle, false otherwise
     */
    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) return true;
        LevelItem li = levelItems[p.getY()][p.getX()];
        return li == LevelItem.TREE || li == LevelItem.MOUNTAIN;
    }

    /**
     * Collects a basket at the given position, if present.
     *
     * @param p the position of the basket
     * @return true if a basket was collected, false otherwise
     */
    public boolean collectBasket(Position p) {
        if (isValidPosition(p) && levelItems[p.getY()][p.getX()] == LevelItem.BASKET) {
            levelItems[p.getY()][p.getX()] = LevelItem.EMPTY;
            basketsCollected++;
            return true;
        }
        return false;
    }

    /**
     * @return true if all baskets in this level are collected, false otherwise
     */
    public boolean allBasketsCollected() {
        return basketsCollected == numBaskets;
    }

    /**
     * @return the entrance position in this level
     */
    public Position getEntrance() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (levelItems[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("No entrance found!");
    }
}
