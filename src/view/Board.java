package view;

import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;

public class Board extends JPanel {

    private final Game game;
    private double scale = 1.0;
    private final int tileSize = 32; // Default size of each tile
    private int scaledSize = tileSize;

    private final EnumMap<LevelItem, Image> itemImages = new EnumMap<>(LevelItem.class);
    private Image rangerImg;
    private Image yogiImg;

    public Board(Game game) {
        this.game = game;
        loadImages();
    }

    /**
     * Load images for all LevelItem types and entities.
     */
    private void loadImages() {
        try {
            itemImages.put(LevelItem.BASKET, ResourceLoader.loadImage("res/basket.png"));
            itemImages.put(LevelItem.TREE, ResourceLoader.loadImage("res/tree.png"));
            itemImages.put(LevelItem.MOUNTAIN, ResourceLoader.loadImage("res/mountain.png"));
            itemImages.put(LevelItem.ENTRANCE, ResourceLoader.loadImage("res/entrance.jpg"));

            rangerImg = ResourceLoader.loadImage("res/ranger.png");
            yogiImg = ResourceLoader.loadImage("res/yogi.png");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load images", e);
        }
    }

    /**
     * Adjust the board scale and refresh the display.
     *
     * @param scale New scale value.
     * @return True if refreshed successfully.
     */
    public boolean setScale(double scale) {
        this.scale = scale;
        this.scaledSize = (int) (scale * tileSize);
        return refresh();
    }

    /**
     * Refresh the board's dimensions and repaint.
     *
     * @return True if the current level is not null.
     */
    public boolean refresh() {
        if (game.getCurrentLevel() == null) return false;

        setPreferredSize(new Dimension(
            game.getCurrentLevel().getCols() * scaledSize,
            game.getCurrentLevel().getRows() * scaledSize
        ));
        repaint();
        return true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (game.getCurrentLevel() == null) return;

        Graphics2D gr = (Graphics2D) g;
        drawLevel(gr);
        drawRangers(gr);
        drawYogi(gr);
    }

    /**
     * Draws the level grid and its items.
     */
    private void drawLevel(Graphics2D gr) {
        int rows = game.getCurrentLevel().getRows();
        int cols = game.getCurrentLevel().getCols();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                LevelItem item = game.getCurrentLevel().getLevel()[y][x];

                if (item == LevelItem.EMPTY) {
                    // Fill empty cells with white background
                    gr.setColor(Color.WHITE);
                    gr.fillRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
                } else {
                    // Draw image for other LevelItems
                    Image img = itemImages.get(item);
                    if (img != null) {
                        gr.drawImage(img, x * scaledSize, y * scaledSize, scaledSize, scaledSize, null);
                    }
                }

                // Draw grid border
                gr.setColor(Color.BLACK);
                gr.drawRect(x * scaledSize, y * scaledSize, scaledSize, scaledSize);
            }
        }
    }

    /**
     * Draws all rangers on the board.
     */
    private void drawRangers(Graphics2D gr) {
        for (var ranger : game.getCurrentLevel().getRangers()) {
            Position rangerPos = ranger.getPosition();
            gr.drawImage(rangerImg, rangerPos.x * scaledSize, rangerPos.y * scaledSize, scaledSize, scaledSize, null);
        }
    }

    /**
     * Draws Yogi's position on the board.
     */
    private void drawYogi(Graphics2D gr) {
        Position yogiPosition = game.getCurrentLevel().getYogi().getPosition();
        gr.drawImage(yogiImg, yogiPosition.x * scaledSize, yogiPosition.y * scaledSize, scaledSize, scaledSize, null);
    }
}
