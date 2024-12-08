package view;

import model.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * Represents the game's menu bar, including options for level selection, restarting, showing the leaderboard, and exiting.
 */
public class GameMenu {

    private final JMenuBar menuBar;

    /**
     * Constructs the GameMenu and initializes its components.
     *
     * @param onLevelSelected    Callback for selecting a level.
     * @param onRestart          Callback for restarting the game.
     * @param onExit             Callback for exiting the game.
     * @param onShowLeaderboard  Callback for showing the leaderboard.
     */
    public GameMenu(
        Consumer<Integer> onLevelSelected,
        Runnable onRestart,
        Runnable onExit,
        Runnable onShowLeaderboard
    ) {
        menuBar = new JMenuBar();

        JMenu menuGame = new JMenu("Game");
        createGameMenu(menuGame, onRestart, onExit, onShowLeaderboard);

        JMenu menuLevelSelect = new JMenu("Select Level");
        createLevelSelectMenu(menuLevelSelect, onLevelSelected);

        menuBar.add(menuGame);
        menuBar.add(menuLevelSelect);
    }

    /**
     * Creates the "Game" menu with options to restart, show the leaderboard, or exit.
     *
     * @param menu                The menu to populate.
     * @param onRestart           Callback for restarting the game.
     * @param onExit              Callback for exiting the game.
     * @param onShowLeaderboard   Callback for showing the leaderboard.
     */
    private void createGameMenu(JMenu menu, Runnable onRestart, Runnable onExit, Runnable onShowLeaderboard) {
        JMenuItem restartItem = new JMenuItem(new AbstractAction("Restart") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRestart.run();
            }
        });
        menu.add(restartItem);

        JMenuItem leaderboardItem = new JMenuItem(new AbstractAction("Leaderboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onShowLeaderboard.run();
            }
        });
        menu.add(leaderboardItem);

        JMenuItem exitItem = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                onExit.run();
            }
        });
        menu.add(exitItem);
    }

    /**
     * Creates the "Select Level" menu with options for selecting a specific level.
     *
     * @param menu             The menu to populate.
     * @param onLevelSelected  Callback for selecting a level.
     */
    private void createLevelSelectMenu(JMenu menu, Consumer<Integer> onLevelSelected) {
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
                        onLevelSelected.accept(levelNumber);
                    }
                }
            });
            menu.add(levelItem);
        }
    }

    /**
     * Returns the menu bar for the game.
     *
     * @return The constructed menu bar.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }
}
