import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AddSetEvent extends ListenerAdapter {

    private final QuizDatabaseManager quizManager = new QuizDatabaseManager();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (message.startsWith("!addset ")) {
            String commandContent = message.substring(8).trim();
            int questionMarkIndex = commandContent.indexOf("?");

            if (questionMarkIndex == -1) {
                event.getChannel().sendMessage("Usage: !addset <question?> <answer>\n\n" +
                "-# Reminder to also include a question mark in your question").queue();
                return;
            }

            // questions are stored without question marks in the database
            String question = commandContent.substring(0, questionMarkIndex).trim();
            String answer = commandContent.substring(questionMarkIndex + 1).trim();

            if (answer.isEmpty()) {
                event.getChannel().sendMessage("Please provide an answer after the question.").queue();
                return;
            }

            quizManager.addSet(question, answer);

            event.getChannel().sendMessage("Question added: \"" + question + "\" with answer: \"" + answer + "\"").queue();
        }
    }
}
