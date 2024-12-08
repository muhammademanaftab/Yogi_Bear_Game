package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtils {

    private final int level;
    private final int rows, cols;
    private final LevelItem[][] levelItems;
    private Yogi yogi;
    private final List<Ranger> rangers = new ArrayList<>();
    private int numBaskets;
    private int basketsCollected;
    private final Random random = new Random();

    public GameUtils(ArrayList<String> gameLevelRows, int level) {
        this.level = level;
        rows = gameLevelRows.size();
        cols = gameLevelRows.stream().mapToInt(String::length).max().orElse(0);

        levelItems = new LevelItem[rows][cols];
        numBaskets = 0;
        basketsCollected = 0;

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
                        if (yogi == null) {
                            yogi = new Yogi(new Position(j, i));
                        }
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
            throw new IllegalStateException("No starting position for Yogi ('Y' or 'E') found in the level!");
        }
    }

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

    private boolean isMovableForRanger(Position p) {
        if (!isValidPosition(p)) {
            return false;
        }
        LevelItem li = levelItems[p.y][p.x];

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

    public Yogi getYogi() {
        return yogi;
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
        return levelItems;
    }

    public int getLevelNumber() {
        return level;
    }

    public boolean isValidPosition(Position p) {
        return p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows;
    }

    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) {
            return true;
        }
        LevelItem li = levelItems[p.y][p.x];
        return li == LevelItem.TREE || li == LevelItem.MOUNTAIN;
    }

    public boolean collectBasket(Position p) {
        if (isValidPosition(p) && levelItems[p.y][p.x] == LevelItem.BASKET) {
            levelItems[p.y][p.x] = LevelItem.EMPTY;
            basketsCollected++;
            return true;
        }
        return false;
    }

    public boolean allBasketsCollected() {
        return basketsCollected == numBaskets;
    }

    public Position getEntrance() {
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (levelItems[y][x] == LevelItem.ENTRANCE) {
                    return new Position(x, y);
                }
            }
        }
        throw new IllegalStateException("No entrance found in the level!");
    }
}
