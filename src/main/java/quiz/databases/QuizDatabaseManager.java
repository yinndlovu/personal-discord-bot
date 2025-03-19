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

    public void updateWinCount(String userId) {
        String query = "UPDATE users SET wins = wins + 1 WHERE user_id = ?";

        try (Connection connection = connector.connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();

            System.out.println("A user win has been updated.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
