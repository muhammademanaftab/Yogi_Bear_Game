package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.WindowConstants;
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
    private boolean isLevelActive = true; // Track if the level is active

    public MainWindow() throws IOException {
    game = new Game();

    setTitle("Yogi Bear Game");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Menu bar setup
    JMenuBar menuBar = new JMenuBar();
    JMenu menuGame = new JMenu("Game");
    JMenu menuGameScale = new JMenu("Scale");
    createLevelMenu(menuGame);
    createScaleMenu(menuGameScale, 1.0, 2.0, 0.5);

    JMenuItem exitItem = new JMenuItem(new AbstractAction("Exit") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });
    menuGame.add(exitItem);
    menuBar.add(menuGame);
    menuBar.add(menuGameScale);
    setJMenuBar(menuBar);

    // Layout setup
    setLayout(new BorderLayout(0, 10));
    gameStatusLabel = new JLabel("Welcome to Yogi Bear Game!");
    add(gameStatusLabel, BorderLayout.NORTH);

    board = new Board(game);
    add(board, BorderLayout.CENTER);

    // Timer for ranger movement (initialize here)
    rangerMovementTimer = new Timer(500, new AbstractAction() { // Adjust delay as needed
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!isRespawning && isLevelActive) { // Only move rangers if level is active
                handleRangerMovement();
            }
        }
    });

    // Load the first level
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

    private void createLevelMenu(JMenu menu) {
        for (int i = 1; i <= Levels.getTotalLevels(); i++) {
            final int levelNumber = i;
            JMenuItem levelItem = new JMenuItem(new AbstractAction("Level " + levelNumber) {
                @Override
                public void actionPerformed(ActionEvent e) {
                    loadLevel(levelNumber);
                }
            });
            menu.add(levelItem);
        }
    }

    private void createScaleMenu(JMenu menu, double from, double to, double by) {
        while (from <= to) {
            final double scale = from;
            JMenuItem scaleItem = new JMenuItem(new AbstractAction(from + "x") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (board.setScale(scale)) {
                        pack();
                    }
                }
            });
            menu.add(scaleItem);
            from += by;
        }
    }

    private void loadLevel(int levelNumber) {
        try {
            ArrayList<String> levelLayout = Levels.getLevel(levelNumber);
            GameUtils selectedLevel = new GameUtils(levelLayout, new GameID("EASY", levelNumber));
            game.loadGame(selectedLevel);
            board.refresh();
            updateStatusLabel();
            pack(); // Adjust window size for new level dimensions

            isLevelActive = true; // Mark the level as active
            rangerMovementTimer.start(); // Restart ranger movement
            JOptionPane.showMessageDialog(this, "Welcome to Level " + levelNumber, "Level Start", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading level: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleKeyPress(KeyEvent e) {
        if (isRespawning || !isLevelActive) return; // Ignore input during respawn or when level is inactive

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
            handleYogiAndRangerCollision(); // Check collision after Yogi moves
            handlePostMoveLogic();         // Check game state after Yogi's move
        }

        board.refresh();
        updateStatusLabel();
    }

    private void handleRangerMovement() {
        game.getCurrentLevel().moveRangersRandomly(); // Rangers move randomly
        handleYogiAndRangerCollision(); // Check collision after ranger moves
        board.refresh();
    }

    private void handleYogiAndRangerCollision() {
        Position yogiPosition = game.getCurrentLevel().getYogi().getPosition();
        Position entrancePosition = game.getCurrentLevel().getEntrance();

        // Ignore collision if Yogi is at the entrance
        if (yogiPosition.equals(entrancePosition)) return;

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
            JOptionPane.showMessageDialog(this, "Game Over! Yogi has no lives left.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {
            isRespawning = true;
            JOptionPane.showMessageDialog(this, "Yogi lost a life! Respawning...", "Life Lost", JOptionPane.WARNING_MESSAGE);
            game.getCurrentLevel().getYogi().setPosition(entrancePosition);
            isRespawning = false;
        }
    }

    private void handlePostMoveLogic() {
        if (game.getCurrentLevel().allBasketsCollected()) {
            isLevelActive = false; // Mark the level as inactive
            rangerMovementTimer.stop(); // Stop ranger movement
            JOptionPane.showMessageDialog(this, "Level Completed! Press OK to load the next level.", "Level Complete", JOptionPane.INFORMATION_MESSAGE);
            loadNextLevel();
            return;
        }

        if (game.isVictory()) {
            isLevelActive = false; // Mark the game as inactive
            rangerMovementTimer.stop(); // Stop ranger movement
            JOptionPane.showMessageDialog(this, "Congratulations! You won the game!", "Victory", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private void loadNextLevel() {
        int currentLevel = game.getCurrentLevel().getGameID().level;
        if (currentLevel >= Levels.getTotalLevels()) {
            JOptionPane.showMessageDialog(this, "You've completed all levels! Game Over.", "Game Complete", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        } else {
            loadLevel(currentLevel + 1);
        }
    }

    private void updateStatusLabel() {
        gameStatusLabel.setText("Lives: " + game.getLives() + ", Score: " + game.getScore());
    }

    public static void main(String[] args) {
        try {
            new MainWindow();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
