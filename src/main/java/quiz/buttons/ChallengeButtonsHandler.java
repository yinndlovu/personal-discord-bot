package quiz.buttons;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChallengeButtonsHandler extends ListenerAdapter {
    
    private final Config config = new Config();
    private final String MY_USER_ID = config.getMyUserId();
    private final String HER_USER_ID = config.getHerUserId();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String compId = event.getComponentId();
        Message message = event.getMessage();
        event.get
        
        if (compId.equals("accept_quiz")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Challenge Accepted");
            embedBuilder.setDescription("The challenge has been accepted!");
            embedBuilder.setColor(Color.GREEN);

            ChallengeHandler challengeHandler = new ChallengeHandler(event.getJDA(), , , event.getTextChannel());

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
