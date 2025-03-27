package quiz.databases;

import data.QuizSet;
import databases.DatabaseConnector;
import java.sql.*;
import java.util.*;

public class QuizDatabaseManager {

    private final DatabaseConnector connector = new DatabaseConnector();

    public ArrayList<QuizSet> getSet() {
        ArrayList<QuizSet> quizSet = new ArrayList<>();
        String query = "SELECT question, answer FROM quiz_set ORDER BY RAND() LIMIT 10";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String question = resultSet.getString("question");
                String answer = resultSet.getString("answer");
                quizSet.add(new QuizSet(question, answer));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return quizSet;
    }

    public void addSet(String question, String answer) {
        String query = "INSERT INTO quiz_set (question, answer) VALUES (?, ?)";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, question);
            preparedStatement.setString(2, answer);
            preparedStatement.executeUpdate();

            System.out.println("A new quiz set has been added.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    public String getQuestion(int id) {
        String query = "SELECT question FROM quiz_set WHERE id = ?";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, id);

            if (resultSet.next()) {
                String question = resultSet.getString("question");
                return question;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
     */

    /*
    public String getAnswer(int id) {
        String query = "SELECT answer FROM quiz_set WHERE id = ?";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet resultSet = preparedStatement.executeQuery()) {
            preparedStatement.setInt(1, id);

            if (resultSet.next()) {
                String answer = resultSet.getString("answer");
                return answer;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
     */

    public int getWinCount(String userId) {
        String sql = "SELECT win_count FROM quiz_users WHERE user_id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("win_count");
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    public String getQuizUser(String userId) {
        String sql = "SELECT user_id FROM quiz_users WHERE user_id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.getString("user_id");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    public void updateWinCount(String userId) {
        String user = getQuizUser(userId);

        if (user == null) {
            String sql = "INSERT INTO quiz_users (user_id, win_count) VALUES (?, ?)";

            try (Connection connection = connector.connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {

                ps.setString(1, userId);
                ps.setInt(2, 1);
                ps.executeUpdate();

                System.out.println("A new quiz player has been added to the database: " + userId + ".");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
            return;
        }

        String query = "UPDATE quiz_users SET win_count = win_count + 1 WHERE user_id = ?";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();

            System.out.println("A user win count has been updated: " + userId + ".");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateGamesPlayed(String userId1, String userId2) {
        String user1 = getQuizUser(userId1);
        String user2 = getQuizUser(userId2);

        try (Connection connection = connector.connect()) {
            if (user1 == null && user2 == null) {
                String sql = "INSERT INTO quiz_users (user_id, games_played) VALUES (?, ?), (?, ?)";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, userId1);
                    ps.setInt(2, 1);
                    ps.setString(3, userId2);
                    ps.setInt(4, 1);
                    ps.executeUpdate();
                }
                System.out.println("Two new quiz players have been added: " + userId1 + " and " + userId2 + ".");

            } else {
                if (user1 == null) {
                    String sql = "INSERT INTO quiz_users (user_id, games_played) VALUES (?, ?)";

                    try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setString(1, userId1);
                        ps.setInt(2, 1);
                        ps.executeUpdate();
                    }
                    System.out.println("A new quiz player has been added: " + userId1 + ".");
                }

                if (user2 == null) {
                    String sql = "INSERT INTO quiz_users (user_id, games_played) VALUES (?, ?)";

                    try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.setString(1, userId2);
                        ps.setInt(2, 1);
                        ps.executeUpdate();
                    }
                    System.out.println("A new quiz player has been added: " + userId2 + ".");
                }
            }

            String sql = "UPDATE quiz_users SET games_played = games_played + 1 WHERE user_id = ?";

            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                if (user1 != null) {
                    ps.setString(1, userId1);
                    ps.executeUpdate();
                }

                if (user2 != null) {
                    ps.setString(1, userId2);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int getGamesPlayed(String userId) {
        String sql = "SELECT games_played FROM quiz_users WHERE user_id = ?";

        try (Connection connection = connector.connect();
            PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.getInt("games_played");
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }
}
