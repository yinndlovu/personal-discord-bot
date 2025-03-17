
package quiz.handlers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ChallengeHandler {
    
    private JDA jda;
    public static String challengerId;
    public static String opponentId;
    private TextChannel channel;
    private int questionIndex = 0;
    public static List<QuizSet> quizSet = quizManager.getQuizSet();
    public static Map<String, Integer> score = new HashMap<>();
    
    public ChallengeHandler(JDA jda, String challengerId, String opponentId, TextChannel channel) {
        this.jda = jda;
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
    }
    
    public void startChallenge() {
        QuizDatabaseManager quizManager = new QuizDatabaseManager();

        score.put(challengerId, 0);
        score.put(opponentId, 0);

        QuizSet currentQuestion = quizSet.get(questionIndex);

        while(questionIndex < quizSet.size()) {
			channel.sendMessage(currentQuestion.getQuestion() + "?");
			questionIndex++;

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.schedule(() -> {
            questionIndex++;
            }, 15, TimeUnit.SECONDS);
		}

        if (score.get(challengerId) > score.get(opponentId)) {
			channel.sendMessage(jda.getUserById(challengerId).getAsMention() + " has won! Congratulations!! ??").queue();
            updateWinCount(challengerId);
		} else if (score.get(challengerId) < score.get(opponentId)) {
			channel.sendMessage(jda.getUserById(opponentId).getAsMention() + " has won! Congratulations!! ??").queue();
            updateWinCount(opponentId);
		} else {
			channel.sendMessage("No one won.").queue();
		}

        channel.sendMessage("SCORE: \n\n" + jda.getUserById(challengerId).getAsMention() + " - " + score.get(challengerId)
            + " | " + jda.getUserById(opponentId).getAsMention() + " - " + score.get(opponentId)).queue();

    }
}
