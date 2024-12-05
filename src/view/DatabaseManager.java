package view;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/yogi_game";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "(Football1498*)";

    public void savePlayerScore(String playerName, int score, long gameDuration) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO player_scores (player_name, score, game_duration) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playerName);
            statement.setInt(2, score);
            statement.setLong(3, gameDuration);
            statement.executeUpdate();
            JOptionPane.showMessageDialog(null, "Your score and time have been saved successfully!", "Score Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error saving score: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<String> getLeaderboard() {
        List<String> leaderboard = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
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
    
    public void resetLeaderboard() {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM player_scores";
            PreparedStatement statement = connection.prepareStatement(sql);
            int rowsAffected = statement.executeUpdate();
            JOptionPane.showMessageDialog(null, rowsAffected + " leaderboard entries have been reset.", "Leaderboard Reset", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error resetting leaderboard: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
