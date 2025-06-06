package giftcards.buttons;

import databases.DatabaseManager;
import essentials.Config;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ClaimButtonHandler extends ListenerAdapter {

    private final Config config = new Config();
    private final String HER_USER_ID = config.getHerUserId();
    private final DatabaseManager databaseManager = new DatabaseManager();

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        try {
            String compId = event.getComponentId();
            String[] parts = compId.split(":");
            
            String action = parts[0];
            String monthName = parts[1];
            
            if (action.equals("claim_button")) {
                if (event.getUser().getId().equals(HER_USER_ID)) {
                    event.deferReply().queue();

                    String giftCard = databaseManager.retrieveGiftCard(monthName).toUpperCase();

                    event.getHook().sendMessage(giftCard).setEphemeral(false).queue();

                    Button disabledButton = Button.success("claim_button", "Claimed").asDisabled();

                    event.getInteraction().getMessage().editMessageEmbeds(event.getInteraction().getMessage().getEmbeds())
                            .setActionRow(disabledButton)
                            .queue();
                } else {
                    event.deferReply().queue();

                    event.getHook().sendMessage("Hooooold your horses bucko! Only <@" + HER_USER_ID + "> "
                            + "can claim the gift card.").setEphemeral(false).queue();
                }
            }
        } catch (Exception ex) {
            System.out.println("Something went wrong in claim the gift card.");
        }
    }
}
