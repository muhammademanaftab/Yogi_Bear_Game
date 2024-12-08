package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import javax.swing.*;
import model.Direction;
import model.Game;
import model.GameUtils;
import model.Levels;
import model.Position;

/**
 * Main game window for the Yogi Bear Game.
 * Handles game initialization, level management, player interactions, and game state updates.
 */
public class MainWindow extends JFrame {

    private final Game game;
    private final DatabaseManager databaseManager;
    private Board board;
    private final JLabel gameStatusLabel;
    private Timer rangerMovementTimer;
    private boolean isRespawning = false;
    private boolean isLevelActive = true;
    private Instant gameStartTime;

    /**
     * Constructs the main game window and initializes the game.
     *
     * @throws IOException if there is an issue loading game resources.
     */
    public MainWindow() throws IOException {
        game = new Game();
        databaseManager = new DatabaseManager();

        setTitle("Yogi Bear Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);

        if (!showAboutPanel()) {
            System.exit(0);
        }

        setJMenuBar(createMenuBar());
        setLayout(new BorderLayout(0, 10));
        gameStatusLabel = new JLabel("Welcome to Yogi Bear Game!");
        add(gameStatusLabel, BorderLayout.NORTH);

        board = new Board(game);
        add(board, BorderLayout.CENTER);

        rangerMovementTimer = new Timer(500, e -> {
            if (!isRespawning && isLevelActive) {
                handleRangerMovement();
            }
        });

        gameStartTime = Instant.now();
        loadLevel(1);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates the menu bar with game and level selection options.
     *
     * @return The constructed menu bar.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGame = new JMenu("Game");
        menuGame.add(new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        }));
        menuGame.add(new JMenuItem(new AbstractAction("Leaderboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                databaseManager.showLeaderboard();
            }
        }));
        menuGame.add(new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        }));
        menuBar.add(menuGame);

        JMenu menuLevelSelect = new JMenu("Select Level");
        int totalLevels = Levels.getTotalLevels();
        for (int i = 1; i <= totalLevels; i++) {
            final int levelNumber = i;
            JMenuItem levelItem = new JMenuItem(new AbstractAction("Level " + levelNumber) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to start Level " + levelNumber + "?",
                        "Confirm Level Selection",
                        JOptionPane.YES_NO_OPTION
                    );
                    if (confirm == JOptionPane.YES_OPTION) {
                        loadLevel(levelNumber);
                    }
                }
            });
            menuLevelSelect.add(levelItem);
        }
        menuBar.add(menuLevelSelect);

        return menuBar;
    }

    /**
     * Displays the about panel with game rules and objective.
     *
     * @return true if the user agrees to proceed, false otherwise.
     */
    private boolean showAboutPanel() {
        String aboutMessage = """
                Welcome to Yogi Bear Game!
                
                Objective:
                - Collect all picnic baskets while avoiding rangers.
                - Enter through the blue entrance to start.
                
                Obstacles:
                - Mountains: Blocks Yogi's path.
                - Trees: Also blocks Yogi's path.
                - Rangers: Moves around; Yogi loses a life if caught.
                
                Visuals:
                - Baskets: Collect them to score points.
                - Yogi: Represents the player.
                - Entrance: Start position.
                
                Rules:
                - Yogi starts with 3 lives.
                - Losing all lives ends the game.
                
                Good luck and have fun!
                """;

        int result = JOptionPane.showConfirmDialog(
                this, aboutMessage, "About Yogi Bear Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }

    /**
     * Restarts the game by resetting the game state and reloading the first level.
     */
    private void restartGame() {
        gameStartTime = Instant.now();
        game.resetGame();
        loadLevel(1);
        isLevelActive = true;
        rangerMovementTimer.start();
        updateStatusLabel();
    }

    /**
     * Exits the game application.
     */
    private void exitGame() {
        System.exit(0);
    }

    /**
     * Loads a specific level in the game.
     *
     * @param levelNumber the level number to load.
     */
    private void loadLevel(int levelNumber) {
        try {
            ArrayList<String> levelLayout = Levels.getLevel(levelNumber);
            GameUtils selectedLevel = new GameUtils(levelLayout, levelNumber);
            game.loadGame(selectedLevel);
            board.refresh();
            updateStatusLabel();
            pack();

            isLevelActive = true;
            rangerMovementTimer.start();
            JOptionPane.showMessageDialog(this, "Welcome to Level " + levelNumber, "Level Start", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading level: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Handles key press events for moving Yogi in the game.
     *
     * @param e the key event triggered by the player.
     */
    private void handleKeyPress(KeyEvent e) {
        if (isRespawning || !isLevelActive) {
            return;
        }

        Direction direction = switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> Direction.UP;
            case KeyEvent.VK_DOWN -> Direction.DOWN;
            case KeyEvent.VK_LEFT -> Direction.LEFT;
            case KeyEvent.VK_RIGHT -> Direction.RIGHT;
            default -> null;
        };

        if (direction != null && game.moveYogi(direction)) {
            handleYogiAndRangerCollision();
            handlePostMoveLogic();
        }

        board.refresh();
        updateStatusLabel();
    }

    /**
     * Handles the random movement of rangers in the game.
     */
    private void handleRangerMovement() {
        game.getCurrentLevel().moveRangersRandomly();
        handleYogiAndRangerCollision();
        board.refresh();
    }

    /**
     * Handles collisions between Yogi and rangers, including life loss logic.
     */
    private void handleYogiAndRangerCollision() {
        Position yogiPosition = game.getCurrentLevel().getYogi().getPosition();
        Position entrancePosition = game.getCurrentLevel().getEntrance();

        if (yogiPosition.equals(entrancePosition)) {
            return;
        }

        for (var ranger : game.getCurrentLevel().getRangers()) {
            if (ranger.getPosition().equals(yogiPosition)) {
                handleYogiLifeLoss();
                return;
            }
        }
    }

    /**
     * Handles the logic for losing a life when Yogi is caught by a ranger.
     */
    private void handleYogiLifeLoss() {
        Position entrancePosition = game.getCurrentLevel().getEntrance();
        game.loseLife();

        if (game.isGameOver()) {
            isLevelActive = false;
            rangerMovementTimer.stop();
            offerToSaveScore("Game Over! Yogi has no lives left.");
            showGameOverDialog("Game Over! Yogi has no lives left.");
        } else {
            isRespawning = true;
            JOptionPane.showMessageDialog(this, "Yogi lost a life! Respawning...", "Life Lost", JOptionPane.WARNING_MESSAGE);
            game.getCurrentLevel().getYogi().setPosition(entrancePosition);
            isRespawning = false;
        }
    }

    /**
     * Handles post-movement logic, such as level completion or victory.
     */
    private void handlePostMoveLogic() {
        if (game.getCurrentLevel().allBasketsCollected()) {
            isLevelActive = false;
            rangerMovementTimer.stop();
            JOptionPane.showMessageDialog(this, "Level Completed! Press OK to load the next level.", "Level Complete", JOptionPane.INFORMATION_MESSAGE);
            loadNextLevel();
        }

        if (game.isVictory()) {
            isLevelActive = false;
            rangerMovementTimer.stop();
            offerToSaveScore("Congratulations! You won the game!");
            showVictoryDialog("Congratulations! You won the game!");
        }
    }

    /**
     * Loads the next level or displays the victory dialog if all levels are completed.
     */
    private void loadNextLevel() {
        int currentLevel = game.getCurrentLevel().getLevelNumber();
        if (currentLevel >= Levels.getTotalLevels()) {
            isLevelActive = false;
            offerToSaveScore("Congratulations! You completed all levels!");
            showVictoryDialog("Congratulations! You completed all levels!");
        } else {
            loadLevel(currentLevel + 1);
        }
    }

    /**
     * Updates the game status label to reflect the current lives and score.
     */
    private void updateStatusLabel() {
        gameStatusLabel.setText("Lives: " + game.getLives() + ", Score: " + game.getScore());
    }

    /**
     * Offers the player the option to save their score to the leaderboard.
     *
     * @param message the message to display in the dialog.
     */
    private void offerToSaveScore(String message) {
        int option = JOptionPane.showConfirmDialog(
                this,
                message + "\nWould you like to save your score to the leaderboard?",
                "Save Score",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (option == JOptionPane.YES_OPTION) {
            savePlayerScore();
        }
    }

    /**
     * Saves the player's score to the database.
     */
    private void savePlayerScore() {
        String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Save Score", JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonymous";
        }
        databaseManager.savePlayerScore(playerName, game.getScore(), Duration.between(gameStartTime, Instant.now()).toSeconds());
    }

    /**
     * Displays the victory dialog with options to restart, view the leaderboard, or exit.
     *
     * @param message the victory message to display.
     */
    private void showVictoryDialog(String message) {
        int option = JOptionPane.showOptionDialog(
                this,
                message + "\nWhat would you like to do?",
                "Victory",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Show Leaderboard", "Restart", "Exit"},
                "Show Leaderboard"
        );

        if (option == 0) {
            databaseManager.showLeaderboard();
            showVictoryDialog(message);
        } else if (option == 1) {
            restartGame();
        } else if (option == 2) {
            exitGame();
        }
    }

    /**
     * Displays the game over dialog with options to restart, view the leaderboard, or exit.
     *
     * @param message the game over message to display.
     */
    private void showGameOverDialog(String message) {
        int option = JOptionPane.showOptionDialog(
                this,
                message + "\nWhat would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Show Leaderboard", "Restart", "Exit"},
                "Restart"
        );

        if (option == 0) {
            databaseManager.showLeaderboard();
            showGameOverDialog(message);
        } else if (option == 1) {
            restartGame();
        } else if (option == 2) {
            exitGame();
        }
    }

    /**
     * Main method to launch the game application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
