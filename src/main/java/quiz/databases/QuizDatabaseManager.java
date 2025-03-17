public class QuizDatabaseManager {

	private final DatabaseConnector connector = new DatabaseConnector();

	public List<QuizSet> getSet() {
		List<QuizSet> quizSet = new ArrayList<>();
		try (Connection connection = connector.connect();
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM quiz_set");
				ResultSet resultSet = preparedStatement.executeQuery()) {

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

	public void addSet (String question, String answer) {
		try (Connection connection = connector.connect();
				PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_set (question, answer) VALUES (?, ?)")) {
			preparedStatement.setString(1, question);
			preparedStatement.setString(2, answer);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getQuestion(int id) {
		try (Connection connection = connector.connect();
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT question FROM quiz_set WHERE id = ?");
				ResultSet resultSet = preparedStatement.executeQuery()) {
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
		try (Connection connection = connector.connect();
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT answer FROM quiz_set WHERE id = ?");
				ResultSet resultSet = preparedStatement.executeQuery()) {
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