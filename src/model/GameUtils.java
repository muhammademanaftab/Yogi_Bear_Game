package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameUtils {
    private final GameID gameID;
    private final int rows, cols;
    private final LevelItem[][] level;
    private Yogi yogi;
    private final List<Ranger> rangers = new ArrayList<>();
    private int numBaskets;
    private int basketsCollected;
    private final Random random = new Random();

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
                    case 'Y':
                        yogi = new Yogi(new Position(j, i));
                        level[i][j] = LevelItem.EMPTY;
                        break;
                    case 'B':
                        numBaskets++;
                        level[i][j] = LevelItem.BASKET;
                        break;
                    case 'T':
                        level[i][j] = LevelItem.TREE;
                        break;
                    case 'M':
                        level[i][j] = LevelItem.MOUNTAIN;
                        break;
                    case 'E':
                        level[i][j] = LevelItem.ENTRANCE;
                        if (yogi == null) {
                            yogi = new Yogi(new Position(j, i));
                        }
                        break;
                    case 'R':
                        rangers.add(new Ranger(new Position(j, i)));
                        level[i][j] = LevelItem.EMPTY;
                        break;
                    default:
                        level[i][j] = LevelItem.EMPTY;
                        break;
                }
            }
        }

        if (yogi == null) {
            throw new IllegalStateException("No starting position for Yogi ('Y' or 'E') found in the level!");
        }
    }

    public void printLevel() {
        System.out.println("\nCurrent Level:");
        Position yogiPosition = yogi.getPosition();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                boolean rangerPrinted = false;
                for (Ranger ranger : rangers) {
                    if (ranger.getPosition().x == x && ranger.getPosition().y == y) {
                        System.out.print("R ");
                        rangerPrinted = true;
                        break;
                    }
                }
                if (rangerPrinted) {
                    continue;
                }

                if (yogiPosition.x == x && yogiPosition.y == y) {
                    System.out.print("Y ");
                } else {
                    System.out.print(level[y][x].representation + " ");
                }
            }
            System.out.println();
        }
    }

    public void moveRangersRandomly() {
        for (Ranger ranger : rangers) {
            Position currentPosition = ranger.getPosition();
            List<Direction> possibleDirections = new ArrayList<>();

            // Check all valid directions for the ranger
            for (Direction d : Direction.values()) {
                Position newPosition = currentPosition.translate(d);
                if (isValidPosition(newPosition) && !isObstacle(newPosition) && level[newPosition.y][newPosition.x] != LevelItem.BASKET) {
                    possibleDirections.add(d);
                }
            }

            // Move to a random valid direction if available
            if (!possibleDirections.isEmpty()) {
                Direction randomDirection = possibleDirections.get(random.nextInt(possibleDirections.size()));
                ranger.setPosition(currentPosition.translate(randomDirection));
            }
        }
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

    public boolean isValidPosition(Position p) {
        return p.x >= 0 && p.y >= 0 && p.x < cols && p.y < rows;
    }

    public boolean isObstacle(Position p) {
        if (!isValidPosition(p)) {
            return true;
        }
        LevelItem li = level[p.y][p.x];
        return li == LevelItem.TREE || li == LevelItem.MOUNTAIN;
    }

    public boolean collectBasket(Position p) {
        if (isValidPosition(p) && level[p.y][p.x] == LevelItem.BASKET) {
            level[p.y][p.x] = LevelItem.EMPTY;
            basketsCollected++;
            return true;
        }
        return false;
    }

    public boolean allBasketsCollected() {
        return basketsCollected == numBaskets;
    }

    public boolean isNearRanger(Position p) {
        for (Ranger ranger : rangers) {
            if (ranger.isNear(p)) {
                return true;
            }
        }
        return false;
    }
}
