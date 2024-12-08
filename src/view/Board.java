package view;

import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import javax.swing.JPanel;
import model.Game;
import model.LevelItem;
import model.Position;
import res.ResourceLoader;

/**
 * Represents the game board where all game elements are displayed.
 */
public class Board extends JPanel {

    private final Game game;
    private int tileSize = 64; // Default tile size
    private final EnumMap<LevelItem, Image> itemImages = new EnumMap<>(LevelItem.class);
    private Image rangerImg;
    private Image yogiImg;

    /**
     * Initializes the board with the provided game instance and loads necessary images.
     *
     * @param game The current game instance.
     */
    public Board(Game game) {
        this.game = game;
        loadImages();
    }

    /**
     * Loads images for all game elements.
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
     * Refreshes the board dimensions based on the game state and screen size.
     *
     * @return True if the board was refreshed successfully, false otherwise.
     */
    public boolean refresh() {
        if (game.getCurrentLevel() == null) return false;

        int rows = game.getCurrentLevel().getRows();
        int cols = game.getCurrentLevel().getCols();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Dynamically adjust tile size based on available screen space
        tileSize = Math.min(screenSize.height / rows / 2, screenSize.width / cols / 2);

        setPreferredSize(new Dimension(cols * tileSize, rows * tileSize));
        revalidate();
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
     * Draws the grid and level items on the board.
     *
     * @param gr The graphics context for drawing.
     */
    private void drawLevel(Graphics2D gr) {
        int rows = game.getCurrentLevel().getRows();
        int cols = game.getCurrentLevel().getCols();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                LevelItem item = game.getCurrentLevel().getLevel()[y][x];

                if (item == LevelItem.EMPTY) {
                    gr.setColor(Color.WHITE);
                    gr.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                } else {
                    Image img = itemImages.get(item);
                    if (img != null) {
                        gr.drawImage(img, x * tileSize, y * tileSize, tileSize, tileSize, null);
                    }
                }

                gr.setColor(Color.BLACK);
                gr.drawRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

    /**
     * Draws all rangers on the board.
     *
     * @param gr The graphics context for drawing.
     */
    private void drawRangers(Graphics2D gr) {
        for (var ranger : game.getCurrentLevel().getRangers()) {
            Position rangerPos = ranger.getPosition();
            gr.drawImage(rangerImg, rangerPos.x * tileSize, rangerPos.y * tileSize, tileSize, tileSize, null);
        }
    }

    /**
     * Draws Yogi's position on the board.
     *
     * @param gr The graphics context for drawing.
     */
    private void drawYogi(Graphics2D gr) {
        Position yogiPosition = game.getCurrentLevel().getYogi().getPosition();
        gr.drawImage(yogiImg, yogiPosition.x * tileSize, yogiPosition.y * tileSize, tileSize, tileSize, null);
    }
}
