package view;
import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class GameMenu {

    private final JMenuBar menuBar;

    public GameMenu(
        Consumer<Integer> onLevelSelected, 
        Runnable onRestart, 
        Runnable onExit, 
        Consumer<Double> onScaleSelected, 
        Runnable onShowLeaderboard
    ) {
        menuBar = new JMenuBar();

        // Game Menu
        JMenu menuGame = new JMenu("Game");
        createGameMenu(menuGame, onRestart, onExit, onShowLeaderboard);

        // Level Selection Menu
        JMenu menuLevelSelect = new JMenu("Select Level");
        createLevelSelectMenu(menuLevelSelect, onLevelSelected);

        // Scale Menu
        JMenu menuGameScale = new JMenu("Scale");
        createScaleMenu(menuGameScale, 1.0, 2.0, 0.5, onScaleSelected);

        menuBar.add(menuGame);
        menuBar.add(menuLevelSelect);
        menuBar.add(menuGameScale);
    }

    private void createGameMenu(JMenu menu, Runnable onRestart, Runnable onExit, Runnable onShowLeaderboard) {
        // Restart Game Menu Item
        JMenuItem restartItem = new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestart.run();
            }
        });
        menu.add(restartItem);

        // Show Leaderboard Menu Item
        JMenuItem leaderboardItem = new JMenuItem(new AbstractAction("Leaderboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShowLeaderboard.run();
            }
        });
        menu.add(leaderboardItem);

        // Exit Game Menu Item
        JMenuItem exitItem = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExit.run();
            }
        });
        menu.add(exitItem);
    }

    private void createLevelSelectMenu(JMenu menu, Consumer<Integer> onLevelSelected) {
        int totalLevels = Levels.getTotalLevels(); // Dynamically fetch the total levels
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
                        onLevelSelected.accept(levelNumber);
                    }
                }
            });
            menu.add(levelItem);
        }
    }

    private void createScaleMenu(JMenu menu, double from, double to, double by, Consumer<Double> onScaleSelected) {
        while (from <= to) {
            final double scale = from;
            JMenuItem scaleItem = new JMenuItem(new AbstractAction(from + "x") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    onScaleSelected.accept(scale);
                }
            });
            menu.add(scaleItem);
            from += by;
        }
    }

    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
