package quiz.handlers;

import data.QuizSet;
import java.util.*;
import java.util.concurrent.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import quiz.databases.QuizDatabaseManager;

public class ChallengeHandler {

    private static final QuizDatabaseManager quizManager = new QuizDatabaseManager();
    private final JDA jda;
    private final String challengerId;
    private final String opponentId;
    private final TextChannel channel;
    private final List<QuizSet> quizSet;
    private final Map<String, Integer> score;
    private int questionIndex = 0;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    public ChallengeHandler(JDA jda, String challengerId, String opponentId, TextChannel channel) {
        this.jda = jda;
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
        this.quizSet = new ArrayList<>(quizManager.getSet());
        this.score = new HashMap<>();
    }

    public void startChallenge() {
        score.put(challengerId, 0);
        score.put(opponentId, 0);
        sendNextQuestion();
    }

    private void sendNextQuestion() {
        if (questionIndex >= quizSet.size()) {
            endChallenge();
            return;
        }

        QuizSet question = quizSet.get(questionIndex);
        channel.sendMessage("**Question " + (questionIndex + 1) + ":** " + question.getQuestion()).queue();

        executor.schedule(() -> {
            questionIndex++;
            sendNextQuestion();
        }, 15, TimeUnit.SECONDS);
    }

    private void endChallenge() {
        String winnerMessage;

        if (score.get(challengerId) > score.get(opponentId)) {
            winnerMessage = jda.getUserById(challengerId).getAsMention() + " has won! 🎉";
            quizManager.updateWinCount(challengerId);
        } else if (score.get(challengerId) < score.get(opponentId)) {
            winnerMessage = jda.getUserById(opponentId).getAsMention() + " has won! 🎉";
            quizManager.updateWinCount(opponentId);
        } else {
            winnerMessage = "It's a tie!";
        }

        channel.sendMessage("**Final Scores:**\n" +
                jda.getUserById(challengerId).getAsMention() + " - " + score.get(challengerId) + "\n" +
                jda.getUserById(opponentId).getAsMention() + " - " + score.get(opponentId) + "\n\n" +
                winnerMessage).queue();

        executor.shutdown();
    }

    public synchronized void recordAnswer(String userId, String answer) {
        if (questionIndex < quizSet.size() && answer.equalsIgnoreCase(quizSet.get(questionIndex).getAnswer())) {
            score.put(userId, score.getOrDefault(userId, 0) + 1);
            channel.sendMessage(jda.getUserById(userId).getAsMention() + " got it right! ✅").queue();
        } else {
            channel.sendMessage("Incorrect answer! ❌").queue();
        }
    }

    public void startChallenge() {
        QuizSession session = new QuizSession(challengerId, opponentId, channel);
        activeSessions.put(channel.getId(), session);

        jda.addEventListener(new QuizAnswerListener(activeSessions));

        session.start();
    }
}
