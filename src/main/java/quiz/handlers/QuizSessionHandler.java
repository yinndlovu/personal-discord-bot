package quiz.handlers;

import data.QuizSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import quiz.sessions.QuizSession;

public class QuizSessionHandler {

    private static final Map<String, QuizSession> activeSessions = new HashMap<>();
    
    public void startSession(String challengerId, String opponentId, TextChannel channel, List<QuizSet> quizSet) {
        String sessionKey = challengerId + "vs" + opponentId;
        QuizSession session = new QuizSession(challengerId, opponentId, channel, quizSet);
        activeSessions.put(sessionKey, session);
        session.askNextQuestion();
    }

    public static QuizSession getSession(String userId) {
        for (QuizSession session : activeSessions.values()) {
            if (session != null && (session.getChallengerId().equals(userId) || session.getOpponentId().equals(userId))) {
                return session;
            }
        }
        return null;
    }
}
