public class QuizSessionHandler {
    private final String challengerId;
    private final String opponentId;
    private final TextChannel channel;
    private final List<Question> questions;
    private final Map<String, Integer> scores;
    private int currentQuestionIndex;

    public QuizSessionHandler(String challengerId, String opponentId, TextChannel channel) {
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
        this.questions = QuestionDatabase.getRandomQuestions(10);
        this.scores = new HashMap<>();
        this.scores.put(challengerId, 0);
        this.scores.put(opponentId, 0);
        this.currentQuestionIndex = 0;
    }

    public void start() {
        channel.sendMessage("?? The quiz is starting!").queue();
        nextQuestion();
    }

    public void nextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            Question q = questions.get(currentQuestionIndex);
            channel.sendMessage("**Question " + (currentQuestionIndex + 1) + ":** " + q.getQuestion()).queue();
        } else {
            channel.sendMessage(getFinalScores()).queue();
        }
    }

    public boolean isCorrectAnswer(String answer) {
        return questions.get(currentQuestionIndex).isCorrectAnswer(answer);
    }

    public void awardPoint(String userId) {
        scores.put(userId, scores.getOrDefault(userId, 0) + 1);
        currentQuestionIndex++;
    }

    public boolean isFinished() {
        return currentQuestionIndex >= questions.size();
    }

    public String getFinalScores() {
        int challengerScore = scores.getOrDefault(challengerId, 0);
        int opponentScore = scores.getOrDefault(opponentId, 0);
        String winner = (challengerScore > opponentScore) ? "<@" + challengerId + ">" :
                        (opponentScore > challengerScore) ? "<@" + opponentId + ">" : "It's a tie!";
        
        return "?? **Final Scores** ??\n" +
               "<@" + challengerId + ">: " + challengerScore + " points\n" +
               "<@" + opponentId + ">: " + opponentScore + " points\n\n" +
               "?? **Winner:** " + winner;
    }
}
