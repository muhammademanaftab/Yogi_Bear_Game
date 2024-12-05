package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class LeaderboardDialog extends JDialog {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/yogi_game";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "(Football1498*)";

    public LeaderboardDialog(JFrame parent) {
        super(parent, "Leaderboard", true);

        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JTable leaderboardTable = new JTable();
        leaderboardTable.setModel(fetchLeaderboardData());

        add(new JScrollPane(leaderboardTable), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }

    private DefaultTableModel fetchLeaderboardData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Rank");
        model.addColumn("Player Name");
        model.addColumn("Score");
        model.addColumn("Game Duration (s)");

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT player_name, score, game_duration " +
                           "FROM player_scores ORDER BY score DESC, game_duration ASC LIMIT 10";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            int rank = 1;
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rank++);
                row.add(resultSet.getString("player_name"));
                row.add(resultSet.getInt("score"));
                row.add(resultSet.getLong("game_duration"));
                model.addRow(row);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching leaderboard: " + ex.getMessage(),
                                          "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }
}
