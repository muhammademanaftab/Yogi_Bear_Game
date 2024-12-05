package view;

import java.awt.BorderLayout;
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
import model.GameID;
import model.Levels;
import model.Position;

public class MainWindow extends JFrame {

    private final Game game;
    private Board board;
    private final JLabel gameStatusLabel;
    private Timer rangerMovementTimer;
    private boolean isRespawning = false;
    private boolean isLevelActive = true;

    private Instant gameStartTime; // To track when the game starts
    private final DatabaseManager databaseManager; // DatabaseManager instance

    public MainWindow() throws IOException {
        game = new Game();
        databaseManager = new DatabaseManager();

        setTitle("Yogi Bear Game");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Show the About panel at the start of the game
        showAboutPanel();

        // Initialize menu
        GameMenu gameMenu = new GameMenu(
                this::loadLevel, // Level selection callback
                this::restartGame, // Restart callback
                this::exitGame, // Exit callback
                this::scaleGame, // Scale callback
                databaseManager::showLeaderboard // Leaderboard callback
        );

        setJMenuBar(gameMenu.getMenuBar());

        // Layout setup
        setLayout(new BorderLayout(0, 10));
        gameStatusLabel = new JLabel("Welcome to Yogi Bear Game!");
        add(gameStatusLabel, BorderLayout.NORTH);

        board = new Board(game);
        board.setScale(1.5); // Set default scale to 1.5
        add(board, BorderLayout.CENTER);

        // Timer for ranger movement
        rangerMovementTimer = new Timer(500, e -> {
            if (!isRespawning && isLevelActive) {
                handleRangerMovement();
            }
        });

        // Load the first level and start the game timer
        gameStartTime = Instant.now();
        loadLevel(1);

        // Key listener for Yogi's movement
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showAboutPanel() {
        String aboutMessage = """
                Welcome to Yogi Bear Game!
                
                Collect all the picnic baskets while avoiding rangers. 
                Yogi has 3 lives and loses one if a ranger gets close.
                Obstacles:
                - Mountains: Grey Triangles
                - Trees: Green Triangles
                - Rangers: Red Circles
                Visuals:
                - Baskets: Yellow Squares
                - Yogi: Yellow Circle
                - Entrance: Blue Rectangle

                Good luck and have fun!
                """;

        JOptionPane.showMessageDialog(
                this,
                aboutMessage,
                "About Yogi Bear Game",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void restartGame() {
        gameStartTime = Instant.now(); // Reset game timer
        loadLevel(1);
    }

    private void exitGame() {
        System.exit(0);
    }

    private void scaleGame(double scale) {
        if (board.setScale(scale)) {
            pack();
        }
    }

    private void loadLevel(int levelNumber) {
        try {
            ArrayList<String> levelLayout = Levels.getLevel(levelNumber);
            GameUtils selectedLevel = new GameUtils(levelLayout, new GameID("EASY", levelNumber));
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

    private void handleKeyPress(KeyEvent e) {
        if (isRespawning || !isLevelActive) {
            return;
        }

        Direction direction = null;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                direction = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                direction = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.RIGHT;
                break;
        }

        if (direction != null && game.moveYogi(direction)) {
            handleYogiAndRangerCollision();
            handlePostMoveLogic();
        }

        board.refresh();
        updateStatusLabel();
    }

    private void handleRangerMovement() {
        game.getCurrentLevel().moveRangersRandomly();
        handleYogiAndRangerCollision();
        board.refresh();
    }

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

    private void handleYogiLifeLoss() {
        Position entrancePosition = game.getCurrentLevel().getEntrance();
        game.loseLife();

        if (game.isGameOver()) {
            showGameOverDialog("Game Over! Yogi has no lives left.");
        } else {
            isRespawning = true;
            JOptionPane.showMessageDialog(this, "Yogi lost a life! Respawning...", "Life Lost", JOptionPane.WARNING_MESSAGE);
            game.getCurrentLevel().getYogi().setPosition(entrancePosition);
            isRespawning = false;
        }
    }

    private void handlePostMoveLogic() {
        if (game.getCurrentLevel().allBasketsCollected()) {
            isLevelActive = false;
            rangerMovementTimer.stop();
            JOptionPane.showMessageDialog(this, "Level Completed! Press OK to load the next level.", "Level Complete", JOptionPane.INFORMATION_MESSAGE);
            loadNextLevel();
            return;
        }

        if (game.isVictory()) {
            isLevelActive = false;
            rangerMovementTimer.stop();
            savePlayerScore();
            showVictoryDialog("Congratulations! You won the game!");
        }
    }

    private void loadNextLevel() {
        int currentLevel = game.getCurrentLevel().getGameID().level;
        if (currentLevel >= Levels.getTotalLevels()) {
            isLevelActive = false;
            savePlayerScore();
            showVictoryDialog("Congratulations! You completed all levels!");
        } else {
            loadLevel(currentLevel + 1);
        }
    }

    private void updateStatusLabel() {
        gameStatusLabel.setText("Lives: " + game.getLives() + ", Score: " + game.getScore());
    }

    private void savePlayerScore() {
        String playerName = JOptionPane.showInputDialog(this, "Enter your name:", "Save Score", JOptionPane.PLAIN_MESSAGE);
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Anonymous";
        }
        int score = game.getScore();
        long gameDuration = Duration.between(gameStartTime, Instant.now()).toSeconds(); // Calculate game duration

        databaseManager.savePlayerScore(playerName, score, gameDuration); // Use DatabaseManager
    }

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

        switch (option) {
            case 0 -> databaseManager.showLeaderboard();
            case 1 -> restartGame();
            case 2 -> exitGame();
        }
    }

    private void showGameOverDialog(String message) {
        int option = JOptionPane.showOptionDialog(
                this,
                message + "\nWhat would you like to do?",
                "Game Over",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Restart", "Exit"},
                "Restart"
        );

        switch (option) {
            case 0 -> restartGame();
            case 1 -> exitGame();
        }
    }

    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
