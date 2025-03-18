package quiz.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import quiz.handlers.QuizSessionHandler;
import quiz.sessions.QuizSession;

public class AnswerEvent extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        QuizSession session = QuizSessionHandler.getSession(userId);

        if (session != null) {
            session.handleAnswer(event);
        }
    }
}
