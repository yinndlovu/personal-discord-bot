package quiz.handlers;

import data.QuizSet;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import quiz.databases.QuizDatabaseManager;

public class ChallengeHandler {

    private static QuizDatabaseManager quizManager = new QuizDatabaseManager();
    private JDA jda;
    public static String challengerId;
    public static String opponentId;
    private TextChannel channel;
    private int questionIndex = 0;
    public static ArrayList<QuizSet> quizSet = quizManager.getSet();
    public static Map<String, Integer> score = new HashMap<>();

    public ChallengeHandler(JDA jda, String challengerId, String opponentId, TextChannel channel) {
        this.jda = jda;
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
    }

    public void startChallenge() {
        score.put(challengerId, 0);
        score.put(opponentId, 0);

        QuizSet currentQuestion = quizSet.get(questionIndex);

        while (questionIndex < quizSet.size()) {
            channel.sendMessage(currentQuestion.getQuestion() + "?");
            questionIndex++;

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
                questionIndex++;
            }, 15, TimeUnit.SECONDS);
        }

        if (score.get(challengerId) > score.get(opponentId)) {
            channel.sendMessage(jda.getUserById(challengerId).getAsMention() + " has won! Congratulations!! ??").queue();
            quizManager.updateWinCount(challengerId);
        } else if (score.get(challengerId) < score.get(opponentId)) {
            channel.sendMessage(jda.getUserById(opponentId).getAsMention() + " has won! Congratulations!! ??").queue();
            quizManager.updateWinCount(opponentId);
        } else {
            channel.sendMessage("No one won.").queue();
        }

        channel.sendMessage("SCORE: \n\n" + jda.getUserById(challengerId).getAsMention() + " - " + score.get(challengerId)
                + " | " + jda.getUserById(opponentId).getAsMention() + " - " + score.get(opponentId)).queue();

    }
}
