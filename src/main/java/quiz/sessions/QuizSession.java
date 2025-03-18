package quiz.sessions;

import data.QuizSet;
import essentials.Config;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QuizSession {

    private final Config config = new Config();
    private final String MY_USER_ID = config.getMyUserId();
    private final String HER_USER_ID = config.getHerUserId();
    private final String challengerId;
    private final String opponentId;
    private final TextChannel channel;
    private final List<QuizSet> quizSet;
    private final Map<String, Integer> scores = new HashMap<>();
    private int currentIndex = 0;
    private final ScheduledExecutorService timer = Executors.newScheduledThreadPool(1);
    private boolean answered = false;
    private ScheduledFuture<?> timeoutTask;

    public QuizSession(String challengerId, String opponentId, TextChannel channel, List<QuizSet> quizSet) {
        this.challengerId = challengerId;
        this.opponentId = opponentId;
        this.channel = channel;
        this.quizSet = quizSet;
        scores.put(challengerId, 0);
        scores.put(opponentId, 0);
    }

    public String getChallengerId() {
        return challengerId;
    }

    public String getOpponentId() {
        return opponentId;
    }

    /*
    public void setInstruction() {
        channel.sendMessage("Please note that the acceptable answers are names, instead of pronouns."
                + "\n\n(ex. If a question asks who cooked? Instead of saying him, say )").queue();
    }
     */
    public void askNextQuestion() {
        if (currentIndex >= quizSet.size()) {
            endQuiz();
            return;
        }

        answered = false;
        QuizSet currentQuestion = quizSet.get(currentIndex);
        channel.sendMessage(currentQuestion.getQuestion() + "?").queue();

        timeoutTask = timer.schedule(() -> {
            if (!answered) {
                String firstCorrectAnswer = currentQuestion.getAnswer().split(",")[0].trim();
                channel.sendMessage("Slow asses. The answer was **" + firstCorrectAnswer + "**.").queue();
                currentIndex++;
                askNextQuestion();
            }
        }, 30, TimeUnit.SECONDS);
    }

    public void handleAnswer(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();

        if (!userId.equals(challengerId) && !userId.equals(opponentId)) {
            return;
        }

        String username = event.getMember() != null
                ? event.getMember().getEffectiveName()
                : event.getAuthor().getName();

        String userAnswer = event.getMessage().getContentRaw();

        try {
            QuizSet currentQuestion = quizSet.get(currentIndex);
            List<String> correctAnswers = Arrays.asList(currentQuestion.getAnswer().split(","));

            boolean isCorrect = correctAnswers.stream().anyMatch(answer -> answer.trim().equalsIgnoreCase(userAnswer));

            if (userId.equals(HER_USER_ID)) {
                if (userAnswer.equalsIgnoreCase("me") || userAnswer.equalsIgnoreCase("her")) {
                    isCorrect = correctAnswers.stream().anyMatch(answer
                            -> answer.trim().equalsIgnoreCase("her") || answer.trim().equalsIgnoreCase("she"));
                }
            } else if (userId.equals(MY_USER_ID)) {
                if (userAnswer.equalsIgnoreCase("me") || userAnswer.equalsIgnoreCase("him")) {
                    isCorrect = correctAnswers.stream().anyMatch(answer
                            -> answer.trim().equalsIgnoreCase("him") || answer.trim().equalsIgnoreCase("he"));
                }
            }

            if (isCorrect) {
                if (timeoutTask != null) {
                    timeoutTask.cancel(false);
                }
                scores.put(userId, scores.get(userId) + 1);
                channel.sendMessage("**" + username + "**: correct! ✅").queue();
                currentIndex++;
                askNextQuestion();
            } else {
                channel.sendMessage("**" + username + "**: wrong! ❌").queue();
            }
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("No more quiz sets to access.");
        }
    }

    private void endQuiz() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        int challengerScore = scores.getOrDefault(challengerId, 0);
        int opponentScore = scores.getOrDefault(opponentId, 0);
        String resultMessage;

        if (challengerScore > opponentScore) {
            resultMessage = "<@" + challengerId + "> wins! 🎉";
        } else if (opponentScore > challengerScore) {
            resultMessage = "<@" + opponentId + "> wins! 🎉";
        } else {
            resultMessage = "Nobody wins...";
        }

        embedBuilder.setTitle("Quiz Score");
        embedBuilder.setDescription("<@" + challengerId + "> - " + challengerScore + "\n"
                + "<@" + opponentId + "> - " + opponentScore + "\n\n"
                + resultMessage);
        embedBuilder.setColor(Color.PINK);

        channel.sendMessage("GAME OVER").addEmbeds(embedBuilder.build()).queue();
    }
}
