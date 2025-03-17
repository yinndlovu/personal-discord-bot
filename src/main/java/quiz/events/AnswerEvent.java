package quiz.events;

import data.QuizSet;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import quiz.handlers.ChallengeHandler;

public class AnswerEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        QuizSet currentAnswer = ChallengeHandler.quizSet.get(answerIndex);
        String answer = event.getMessage().getContentRaw();

        if (answer.equalsIgnoreCase(currentAnswer.getAnswer)) {
            event.getChannel().sendMessage("Correct!").queue();
            answerIndex++;

            if (event.getAuthot().getId().equals(ChallengeHandler.challengerId)) {
                ChallengeHandler.score.put(ChallengeHandler.challengerId,
                        ChallengeHandler.score.get(ChallengeHandler.challengerId) + 1);
            } else if (event.getAuthor().getId().equals(ChallengeHandler.opponentId)) {
                ChallengeHandler.score.put(ChallengeHandler.opponentId,
                        ChallengeHandler.score.get(ChallengeHandler.opponentId) + 1);
            }
        } else {
            event.getChannel().sendMessage("Incorrect!").queue();
        }
    }
}
