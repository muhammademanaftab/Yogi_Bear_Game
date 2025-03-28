package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

/**
 * Displays a leaderboard in a dialog window, fetching data from the database.
 */
public class LeaderboardDialog extends JDialog {

    private static final String dataBaseURL = "jdbc:mysql://localhost:3306/yogi_game";
    private static final String dataBaseUSer = "root";
    private static final String dataBasePassKey = "(Football1498*)";

    /**
     * Constructs the leaderboard dialog window.
     *
     * @param parent The parent frame from which this dialog is displayed.
     */
    public LeaderboardDialog(JFrame parent) {
        super(parent, "Leaderboard", true);
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);

        JTable leaderboardTable = new JTable();
        leaderboardTable.setModel(fetchData());

        add(new JScrollPane(leaderboardTable), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);
    }

    /**
     * Fetches leaderboard data from the database and populates a table model.
     *
     * @return A DefaultTableModel containing the leaderboard data.
     */
    private DefaultTableModel fetchData() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Rank");
        model.addColumn("Player Name");
        model.addColumn("Score");
        model.addColumn("Game Duration (s)");

        try (Connection connection = DriverManager.getConnection(dataBaseURL, dataBaseUSer, dataBasePassKey)) {
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
