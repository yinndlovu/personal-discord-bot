package quiz.buttons;

import essentials.Config;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import quiz.handlers.ChallengeHandler;

public class ChallengeButtonsHandler extends ListenerAdapter {
    
    private final Config config = new Config();
    private final String MY_USER_ID = config.getMyUserId();
    private final String HER_USER_ID = config.getHerUserId();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String compId = event.getComponentId();
        Message message = event.getMessage();
        
        String[] parts = compId.split(":");

        if (parts.length < 3) {
            return;
        }

        String action = parts[0];
        String challengerId = parts[1];
        String opponentId = parts[2];

        if (action.equals("accept_quiz")) {
            
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Challenge Accepted");
            embedBuilder.setDescription("The challenge has been accepted!");
            embedBuilder.setColor(Color.GREEN);

            ChallengeHandler challengeHandler = new ChallengeHandler(event.getJDA(), challengerId, opponentId, event.getChannel.getAsTextChannel());

        } else if (compId.equals("reject_quiz")) {

            EmbedBuilder embedBuilder = new EmbedBuilder();
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
