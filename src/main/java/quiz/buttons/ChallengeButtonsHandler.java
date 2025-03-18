package quiz.buttons;

import data.QuizSet;
import essentials.Config;
import java.awt.Color;
import java.util.List;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import quiz.databases.QuizDatabaseManager;
import quiz.handlers.QuizSessionHandler;

public class ChallengeButtonsHandler extends ListenerAdapter {

    private final Config config = new Config();
    private final String MY_USER_ID = config.getMyUserId();
    private final String HER_USER_ID = config.getHerUserId();
    private final QuizDatabaseManager quizManager = new QuizDatabaseManager();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String compId = event.getComponentId();
        Message message = event.getMessage();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String[] parts = compId.split(":");

        if (parts.length < 3) {
            return;
        }

        String action = parts[0];
        String challengerId = parts[1];
        String opponentId = parts[2];

        if (!event.getUser().getId().equals(opponentId)) {
            return;
        }

        event.deferEdit().queue();

        if (action.equals("accept_quiz")) {

            embedBuilder.setTitle("Challenge Accepted");
            embedBuilder.setDescription("The challenge has been accepted!");
            embedBuilder.setColor(Color.GREEN);

            TextChannel channel = event.getChannel().asTextChannel();
            List<QuizSet> quizSet = quizManager.getSet();

            QuizSessionHandler sessionHandler = new QuizSessionHandler();
            sessionHandler.startSession(challengerId, opponentId, channel, quizSet);

        } else if (action.equals("decline_quiz")) {

            embedBuilder.setTitle("Challenge Rejected");

            if (event.getUser().getId().equals(MY_USER_ID)) {
                embedBuilder.setDescription("Challenge rejected. Scared little boy...");
            } else if (event.getUser().getId().equals(HER_USER_ID)) {
                embedBuilder.setDescription("Challenge rejected. Scared little girl...");
            } else {
                embedBuilder.setDescription("The challenge has been rejected.");
            }
            embedBuilder.setColor(Color.RED);
        }

        message.editMessageEmbeds(embedBuilder.build())
                .setActionRow(
                        Button.primary("accept_quiz", "Game on").asDisabled(),
                        Button.danger("decline_quiz", "Not now").asDisabled()
                ).queue();
    }
}
