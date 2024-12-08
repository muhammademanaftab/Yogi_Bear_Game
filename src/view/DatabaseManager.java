package view;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database operations such as saving scores, retrieving the leaderboard, and resetting the leaderboard.
 */
public class DatabaseManager {

    private static final String dataBaseUrl = "jdbc:mysql://localhost:3306/yogi_game";
    private static final String dataBaseUSer = "root";
    private static final String dataBasePassKey = "(Football1498*)";

    /**
     * Saves a player's score and game duration to the database. If the database already contains 10 entries, the oldest entry is removed.
     *
     * @param playerName   The name of the player.
     * @param score        The player's score.
     * @param gameDuration The duration of the game in seconds.
     */
    public void savePlayerScore(String playerName, int score, long gameDuration) {
        try (Connection connection = DriverManager.getConnection(dataBaseUrl, dataBaseUSer, dataBasePassKey)) {
            String countQuery = "SELECT COUNT(*) FROM player_scores";
            Statement countStatement = connection.createStatement();
            ResultSet countResult = countStatement.executeQuery(countQuery);
            countResult.next();
            int totalEntries = countResult.getInt(1);

            if (totalEntries >= 10) {
                String deleteOldestQuery = "DELETE FROM player_scores ORDER BY id ASC LIMIT 1";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteOldestQuery);
                deleteStatement.executeUpdate();
            }

            String insertQuery = "INSERT INTO player_scores (player_name, score, game_duration) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, playerName);
            insertStatement.setInt(2, score);
            insertStatement.setLong(3, gameDuration);
            insertStatement.executeUpdate();

            JOptionPane.showMessageDialog(null, "Your score and time have been saved successfully!", "Score Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error saving score: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retrieves the leaderboard from the database and returns it as a list of formatted strings.
     *
     * @return A list of leaderboard entries in the format "PlayerName - Score: X, Time: Ys".
     */
    public List<String> getLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(dataBaseUrl, dataBaseUSer, dataBasePassKey)) {
            String sql = "SELECT player_name, score, game_duration FROM player_scores ORDER BY score DESC, game_duration ASC";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("player_name");
                int score = resultSet.getInt("score");
                long duration = resultSet.getLong("game_duration");
                leaderboard.add(String.format("%s - Score: %d, Time: %ds", name, score, duration));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving leaderboard: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return leaderboard;
    }

    /**
     * Displays the leaderboard in a dialog box. If no scores are available, shows a message indicating the leaderboard is empty.
     */
    public void showLeaderboard() {
        List<String> leaderboard = getLeaderboard();
        if (leaderboard.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No scores available!", "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder leaderboardText = new StringBuilder("Leaderboard:\n\n");
        for (int i = 0; i < leaderboard.size(); i++) {
            leaderboardText.append((i + 1)).append(". ").append(leaderboard.get(i)).append("\n");
        }

        JOptionPane.showMessageDialog(null, leaderboardText.toString(), "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Resets the leaderboard by deleting all entries from the database.
     */
    public void resetLeaderboard() {
        try (Connection connection = DriverManager.getConnection(dataBaseUrl, dataBaseUSer, dataBasePassKey)) {
            String sql = "DELETE FROM player_scores";
            PreparedStatement statement = connection.prepareStatement(sql);
            int rowsAffected = statement.executeUpdate();
            JOptionPane.showMessageDialog(null, rowsAffected + " leaderboard entries have been reset.", "Leaderboard Reset", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error resetting leaderboard: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
