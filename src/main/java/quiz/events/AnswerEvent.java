public class QuizAnswerListener extends ListenerAdapter {

    private final Map<String, QuizSession> activeSessions;

    public QuizAnswerListener(Map<String, QuizSession> activeSessions) {
        this.activeSessions = activeSessions;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String channelId = event.getChannel().getId();
        QuizSession session = activeSessions.get(channelId);

        if (session == null) return;

        String userId = event.getAuthor().getId();
        String answer = event.getMessage().getContentRaw().trim();

        if (session.isCorrectAnswer(answer)) {
            session.awardPoint(userId);
            event.getChannel().sendMessage("✅ Correct! Next question...").queue();
            session.nextQuestion();

            if (session.isFinished()) {
                event.getChannel().sendMessage(session.getFinalScores()).queue();
                activeSessions.remove(channelId);
            }
        } else {
            event.getChannel().sendMessage("❌ Wrong answer! Try again.").queue();
        }
    }
}
