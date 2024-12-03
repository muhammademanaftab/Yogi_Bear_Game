package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;

public class Board extends JPanel {

    private final Game game;
    private double scale;
    private int scaledSize;
    private final int tileSize = 32; // Default size of each tile

    public Board(Game game) throws IOException {
        this.game = game;
        scale = 1.0;
        scaledSize = (int) (scale * tileSize);
    }

    public boolean setScale(double scale) {
        this.scale = scale;
        scaledSize = (int) (scale * tileSize);
        return refresh();
    }

    public boolean refresh() {
        if (game.getCurrentLevel() == null) return false;

        // Update the dimensions based on the current level's size
        Dimension dim = new Dimension(
            game.getCurrentLevel().getCols() * scaledSize,
            game.getCurrentLevel().getRows() * scaledSize
        );
        setPreferredSize(dim);
        setMaximumSize(dim);
        setSize(dim);
        repaint();
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Ensure the panel is properly cleared
        if (game.getCurrentLevel() == null) return;

        Graphics2D gr = (Graphics2D) g;
        int rows = game.getCurrentLevel().getRows();
        int cols = game.getCurrentLevel().getCols();

        // Draw the level grid and items
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                LevelItem item = game.getCurrentLevel().getLevel()[y][x];

                // Assign colors based on level item
                switch (item) {
                    case BASKET:
                        gr.setColor(Color.YELLOW);
                        gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                        break;
                    case TREE:
                        gr.setColor(new Color(34, 139, 34)); // Forest green
                        gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                        break;
                    case MOUNTAIN:
                        gr.setColor(Color.GRAY);
                        gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                        break;
                    case ENTRANCE:
                        gr.setColor(Color.BLUE);
                        gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                        break;
                    case EMPTY:
                        gr.setColor(Color.WHITE);
                        gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                        break;
                }

                // Draw grid lines for better visual clarity
                gr.setColor(Color.BLACK);
                gr.drawRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
            }
        }

        // Draw rangers
        gr.setColor(Color.RED);
        for (var ranger : game.getCurrentLevel().getRangers()) {
            Position rangerPos = ranger.getPosition();
            gr.fillOval(rangerPos.x * scaledSize, rangerPos.y * scaledSize, scaledSize, scaledSize);
        }

        // Draw Yogi's position
        Position yogiPosition = game.getCurrentLevel().getYogi().getPosition();
        gr.setColor(Color.ORANGE);
        gr.fillOval(yogiPosition.x * scaledSize, yogiPosition.y * scaledSize, scaledSize, scaledSize);
    }
}
